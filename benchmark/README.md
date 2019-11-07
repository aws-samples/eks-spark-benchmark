### Spark on Kubernetes benchmark utility

This example is used to benchmarks Spark performance on Kubernetes.


### Prerequisites



### Build project

#### 1. Build Dependencies
- [spark-sql-perf](https://github.com/databricks/spark-sql-perf)

Latest maven version is 0.3.2 which is too old.
Please build with `sbt +package` and get `spark-sql-perf_2.12-0.5.1-SNAPSHOT.jar`. Copy jars to `libs/`.

```
git clone https://github.com/databricks/spark-sql-perf
cd spark-sql-perf
sbt +package
```


#### 1. Build benchmark utility

```
$ sbt assembly
```

#### 1. Build container image

```
IMAGE_TAG=v0.1-$(date +'%Y%m%d')
docker build -t seedjeffwan/spark-benchmark:$IMAGE_TAG .
```

If you like to build based on a different spark base image.

```
docker build -t seedjeffwan/spark-benchmark:$IMAGE_TAG --build-arg SPARK_BASE_IMAGe=your_spark_image .
```

### Run on Kubernetes cluster

We highly recommend you use [spark-operator](https://github.com/GoogleCloudPlatform/spark-on-k8s-operator) to manage spark applications.
In the benchmark, we use s3 to host TPC-DS dataset and also export query output to S3. Please make sure you attach
`AmazonS3FullAccess` policy in your EKS node group role.


#### 1. Prepare the TCP-DS data

`com.amazonaws.eks.tpcds.DataGeneration`


#### 2. Run TPC-DS queries

`com.amazonaws.eks.tpcds.BenchmarkSQL`

Please check examples folder for more details


### Credits

TPC-DS and TeraSort is pretty popular in big data area and there're few existing solutions.
Some codes in the example come from @Kisimple [here](https://github.com/kisimple/spark/tree/terasort/examples/src/main/scala/org/apache/spark/examples/terasort) and example from @cern [here](https://gitlab.cern.ch/db/spark-service/spark-k8s-examples).
