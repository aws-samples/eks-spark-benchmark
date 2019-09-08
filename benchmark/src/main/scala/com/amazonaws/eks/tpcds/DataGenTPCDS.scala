package com.amazonaws.eks.tpcds

import com.databricks.spark.sql.perf.tpcds.TPCDSTables
import org.apache.log4j.{Level, LogManager}
import org.apache.spark.sql.SparkSession

import scala.util.Try

object DataGenTPCDS {
  def main(args: Array[String]) {
    val tpcdsDataDir = args(0)
    val dsdgenDir = args(1)
    val scaleFactor = Try(args(2).toString).getOrElse("1")
    val genPartitions = Try(args(3).toInt).getOrElse(100)
    val partitionTables = Try(args(4).toBoolean).getOrElse(false)
    val clusterByPartitionColumns = Try(args(5).toBoolean).getOrElse(false)
    val onlyWarn = Try(args(6).toBoolean).getOrElse(false)

    val format = "parquet"

    println(s"DATA DIR is $tpcdsDataDir")
    println(s"Tools dsdgen executable located in $dsdgenDir")
    println(s"Scale factor is $scaleFactor GB")

    val spark = SparkSession
      .builder
      .appName(s"TPCDS DataGen $scaleFactor GB")
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

    println(s"Generating TPCDS data")

    tables.genData(
      location = tpcdsDataDir,
      format = format,
      overwrite = true,
      partitionTables = partitionTables,
      clusterByPartitionColumns = clusterByPartitionColumns,
      filterOutNullPartitionValues = false,
      tableFilter = "",
      numPartitions = genPartitions)

    println(s"Data generated at $tpcdsDataDir")

    spark.stop()
  }
}