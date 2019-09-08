package com.amazonaws.eks.tpcds

import com.databricks.spark.sql.perf.tpcds.{TPCDS, TPCDSTables}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.col
import org.apache.log4j.{Level, LogManager}
import scala.util.Try

object BenchmarkSparkSQL {
  def main(args: Array[String]) {
    val tpcdsDataDir = args(0)
    val resultLocation = args(1)
    val dsdgenDir = args(2)
    val scaleFactor = Try(args(3).toString).getOrElse("1")
    val iterations = args(4).toInt
    val optimizeQueries = Try(args(5).toBoolean).getOrElse(false)
    val filterQueries = Try(args(6).toString).getOrElse("")
    val onlyWarn = Try(args(7).toBoolean).getOrElse(false)

    val databaseName = "tpcds_db"
    val format = "parquet"
    val timeout = 24*60*60

    println(s"DATA DIR is $tpcdsDataDir")

    val spark = SparkSession
      .builder
      .appName(s"TPCDS SQL Benchmark $scaleFactor GB")
      .getOrCreate()

    if (onlyWarn) {
      println(s"Only WARN")
      LogManager.getLogger("org").setLevel(Level.WARN)
    }

    val tables = new TPCDSTables(spark.sqlContext,
      dsdgenDir = dsdgenDir,
      scaleFactor = scaleFactor,
      useDoubleForDecimal = false,
      useStringForDate = false)

    if (optimizeQueries) {
      Try {
        spark.sql(s"create database $databaseName")
      }
      tables.createExternalTables(tpcdsDataDir, "parquet", databaseName, overwrite = true, discoverPartitions = true)
      tables.analyzeTables(databaseName, analyzeColumns = true)
      spark.conf.set("spark.sql.cbo.enabled", "true")
    } else {
      tables.createTemporaryTables(tpcdsDataDir, "parquet")
    }

    val tpcds = new TPCDS(spark.sqlContext)

    var query_filter : Seq[String] = Seq()
    if (!filterQueries.isEmpty) {
      println(s"Running only queries: $filterQueries")
      query_filter = filterQueries.split(",").toSeq
    }

    val filtered_queries = query_filter match {
      case Seq() => tpcds.tpcds2_4Queries
      case _ => tpcds.tpcds2_4Queries.filter(q => query_filter.contains(q.name))
    }

    // Start collection SparkMeasure
    val stageMetrics = ch.cern.sparkmeasure.StageMetrics(spark)
    stageMetrics.begin()

    // Start experiment
    val experiment = tpcds.runExperiment(
      filtered_queries,
      iterations = iterations,
      resultLocation = resultLocation,
      forkThread = true)

    experiment.waitForFinish(timeout)

    // Collect general results
    val resultPath = experiment.resultPath
    println(s"Reading result at $resultPath")
    val specificResultTable = spark.read.json(resultPath)
    specificResultTable.show()

    // Summarize results
    val result = specificResultTable
      .withColumn("result", explode(col("results")))
      .withColumn("executionSeconds", col("result.executionTime")/1000)
      .withColumn("queryName", col("result.name"))
    result.select("iteration", "queryName", "executionSeconds").show()
    println(s"Final results at $resultPath")
    val aggResults = result.groupBy("queryName").agg(
      callUDF("percentile", col("executionSeconds").cast("long"), lit(0.5)).as('medianRuntimeSeconds),
      callUDF("min", col("executionSeconds").cast("long")).as('minRuntimeSeconds),
      callUDF("max", col("executionSeconds").cast("long")).as('maxRuntimeSeconds)
    ).orderBy(col("queryName"))
    aggResults.repartition(1).write.csv(s"$resultPath/summary.csv")
    aggResults.show(105)

    // Collect SparkMeasure
    stageMetrics.end()
    stageMetrics.printReport()
    spark.table("PerfStageMetrics").repartition(1).write.format("csv").option("header", "true").save(s"$resultPath/sparkmeasure.csv")
    spark.stop()
  }
}