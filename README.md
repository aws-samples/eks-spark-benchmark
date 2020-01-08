This repository contains benchmark results and best practice to run Spark workloads on EKS.

## Spark on Kubernetes

Kubernetes is a fast growing open-source platform which provides container-centric infrastructure. Kubernetes has first class support on Amazon Web Services and Amazon Elastic Kubernetes Service (Amazon EKS) is a fully managed Kubernetes service.

Traditionally, data processing workloads have been run in dedicated setups like the YARN/Hadoop stack. However, unifying the control plane for all workloads on Kubernetes simplifies cluster management and can improve resource utilization. Engineers across several companies and organizations have been working on Kubernetes resource manager support as a cluster scheduler backend within Spark.

Starting with Spark 2.3, users can run Spark workloads in an existing Kubernetes cluster and take advantage of Apache Spark’s ability to manage distributed data processing tasks.

In order to run large scale spark applications in Kubernetes, there's still a lots of performance issues in Spark 2.4 or 3.0 we'd like users to know. This repo will talk about these performance optimization and best practice moving Spark workloads to Kubernetes.


## TPC-DS Benchmark

Created by a third-party committee, TPC-DS is the de-facto industry standard benchmark for measuring the performance of decision support solutions. According to its own homepage (https://www.tpc.org/tpcds/), it defines decision support systems as those that examine large volumes of data, give answers to real-world business questions, execute SQL queries of various operational requirements and complexities (e.g., ad-hoc, reporting, iterative OLAP, data mining), and are characterized by high CPU and IO load.

This benchmark includes 104 queries that exercise a large part of the SQL 2003 standards – 99 queries of the TPC-DS benchmark, four of which with two variants (14, 23, 24, 39) and “s_max” query performing a full scan and aggregation of the biggest table, store_sales.

We can evaluate and measure the performance of Spark SQL using the TPC-DS benchmark on EKS.


Overall, we need some explanation here.


## Getting Started

To replicate benchmark on EKS cluster, please follow [instructions](./benchmark/README.md)


## Perforamnce hits and optimization

- [Kubernetes Cluster Optimization](./performance/kubernetes.md)

- [Shuffle Performance Improvement](./performance/shuffle.md)

- [S3A Committers](./performance/s3.md)

- [Customized Schedulers](./performance/scheduler.md)

