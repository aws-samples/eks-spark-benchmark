/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.amazonaws.eks.terasort

import java.util.Comparator

import com.google.common.primitives.UnsignedBytes

import org.apache.spark.RangePartitioner
import org.apache.spark.sql.SparkSession

// scalastyle:off println
object TeraSort {

  implicit val caseInsensitiveOrdering : Comparator[Array[Byte]] =
    UnsignedBytes.lexicographicalComparator()

  def main(args: Array[String]) {

    if (args.length != 2) {
      println("Usage:")
      println("bin/spark-submit " +
        "--class org.apache.spark.examples.terasort.TeraSort " +
        "examples/jars/spark-examples*.jar " +
        "[HDFSInputPath] [HDFSOutputPath]")
      System.exit(0)
    }

    val inputPath = args(0)
    val outputPath = args(1)

    val spark = SparkSession
      .builder()
      .appName("TeraSort")
      .getOrCreate()
    val sc = spark.sparkContext

    val inputFiles = sc.newAPIHadoopFile[Array[Byte], Array[Byte], TeraInputFormat](inputPath)
    val partitioner = new RangePartitioner(sc.defaultParallelism, inputFiles, true)
    val sortedRDD = inputFiles.repartitionAndSortWithinPartitions(partitioner)
    sortedRDD.saveAsNewAPIHadoopFile[TeraOutputFormat](outputPath)

  }

}
// scalastyle:on println