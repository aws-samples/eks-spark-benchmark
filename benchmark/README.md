## Spark on Kubernetes benchmark utility

This example is used to benchmark Spark performance on Kubernetes.

## Prerequisite

### Install SBT

This project uses sbt to compile scale codes, please install sbt [here](https://www.scala-sbt.org/1.x/docs/Setup.html)

### Prepare Spark Base Container Image

> Note: Please follow instructions only if you want to build your own image, you can also use provided ones like
`seedjeffwan/spark:2.4.5-SNAPSHOT` or `seedjeffwan/spark:3.0.0-SNAPSHOT`

1. Build Spark Base Image

To create a Spark distribution like those distributed by the [Spark Downloads] page, and that is laid out so as to be runnable, use ./dev/make-distribution.sh in the project root directory. It can be configured with Maven profile settings and so on like the direct Maven build. Example:

```
./dev/make-distribution.sh --name custom-spark --pip --r --tgz -Psparkr -Phadoop-2.7 -Phive -Phive-thriftserver -Pmesos -Pyarn -Pkubernetes
```

This will build Spark distribution along with Python pip and R packages. For more information on usage, run ./dev/make-distribution.sh --help

2. Add `hadoop-aws` and `aws-java-sdk-bundle` library

Based on the the Hadoop version pre-built into Spark, you need to use right version of `hadoop-aws` and `aws-java-sdk-bundle`. Using 2.7.6 as an example,

```
cd ${project_location}/docker/hadoop-aws-2.7.6/

docker build -t <your_username>/spark:2.4.4 .
```

This will give you `your_username/spark:2.4.4` with AWS S3 SDK libraries.


### Attach S3 policy to node group roles

In the benchmark, we use s3 to host TPC-DS dataset and also export query output to S3. Please make sure you attach
`AmazonS3FullAccess` policy in your EKS node group role.


## Build benchamrk project

### Build Dependencies
- [spark-sql-perf](https://github.com/databricks/spark-sql-perf)

Latest maven version is 0.3.2 which is too old.
Please build with `sbt +package` and get `spark-sql-perf_2.12-0.5.1-SNAPSHOT.jar`. Copy jars to `libs/`.

```
git clone https://github.com/databricks/spark-sql-perf
cd spark-sql-perf
sbt +package
```

### Build benchmark utility

```
$ sbt assembly
```

### Build container image

```
IMAGE_TAG=v0.1-$(date +'%Y%m%d')
docker build -t seedjeffwan/spark-benchmark:$IMAGE_TAG .
```

If you like to build based on a different spark base image.

```
docker build -t seedjeffwan/spark-benchmark:$IMAGE_TAG --build-arg SPARK_BASE_IMAGE=your_spark_image .
```

## Install Spark-Operator

We highly recommend you use [spark-operator](https://github.com/GoogleCloudPlatform/spark-on-k8s-operator) to manage spark applications. Please check [installation guidance](https://github.com/GoogleCloudPlatform/spark-on-k8s-operator#installation) to install spark-operator.

Here's an example to use `seedjeffwan/spark-operator:v2.4.5-SNAPSHOT` as your spark operator image.
```
helm install incubator/sparkoperator --namespace spark-operator --set enableWebhook=true --set sparkJobNamespace=default --set operatorImageName=seedjeffwan/spark-operator --set operatorVersion=v2.4.5-SNAPSHOT
```

## Run Benchmark

### Generate the TCP-DS data

```shell
kubectl apply -f examples/tpcds-data-generation.yaml
```

### Run TPC-DS queries

```shell
kubectl apply -f examples/tpcds-benchmark.yaml
```

## Credits

TPC-DS and TeraSort is pretty popular in big data area and there're few existing solutions.
Some codes in the example come from @Kisimple [here](https://github.com/kisimple/spark/tree/terasort/examples/src/main/scala/org/apache/spark/examples/terasort) and example from @cern [here](https://gitlab.cern.ch/db/spark-service/spark-k8s-examples).
