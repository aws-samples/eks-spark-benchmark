## Spark on Kubernetes benchmark utility

This repository is used to benchmark Spark performance on Kubernetes.

We highly recommend you to use our provided images like `seedjeffwan/spark:v2.4.5-examples` or `seedjeffwan/spark:3.0.0-SNAPSHOT-examples` because it has some critital performance improvements which are not in the 2.4.5 distribution yet. Features like `tmpfs` and `hostPath` volume for spark scratch space are not avalable in v2.4.5.

If you want to use our image, you can skip building image section and jump to [Install Spark-Operator](#install-spark-operator) directly.

`seedjeffwan/spark:v2.4.5-examples` use Scala 2.12, it includes `hadoop-aws-3.1.0.jar` and `aws-java-sdk-bundle-1.11.271.jar` for S3A FileSystem.

## Prerequisite

### Attach S3 policy to node group roles

In the benchmark, we use s3 to host TPC-DS dataset and also export query output to S3. Please make sure you attach
`AmazonS3FullAccess` policy in your EKS node group role.

### Install SBT

This project uses sbt to compile scale codes, please install sbt [here](https://www.scala-sbt.org/1.x/docs/Setup.html)

### Prepare Spark Base Container Image

There're two ways to get Spark codes, either from prebuilt Apache Spark for fixed Apache Hadoop version or build your own Spark.

#### Prebuilt Apache Spark

1. Export your dockerhub username

  ```shell
  export DOCKERHUB_USERNAME=<your_dockerhub_username>
  ```

2. Build Spark Base Image from prebuilt Apache Spark

  Go to [Spark Downloads](https://spark.apache.org/downloads.html) page and download latest version, in this case, we download `spark-2.4.5-bin-hadoop2.7.tgz` and unzip the file. This binary uses scala 2.11 and we will use 2.11 for all other applications.

  Run following command to  build your Spark image.

  ```shell
  cd spark-2.4.5-bin-hadoop2.7
  ./bin/docker-image-tool.sh -r $DOCKERHUB_USERNAME -t v2.4.5 build
  ```

  You will get images like `$DOCKERHUB_USERNAME/spark:v2.4.5`, `$DOCKERHUB_USERNAME/spark-py:v2.4.5` and `$DOCKERHUB_USERNAME/spark-r:v2.4.5`.

3. Add `hadoop-aws` and `aws-java-sdk-bundle` library

  Based on the the Hadoop version pre-built into Spark, you need to use right version of `hadoop-aws` and `aws-java-sdk-bundle`. Using 2.7.6 as an example,

  ```
  cd ${project_location}/docker/hadoop-aws-2.7.6/

  docker build -t $DOCKERHUB_USERNAME/spark:v2.4.5-s3 --build-arg BASE_IMAGE=$DOCKERHUB_USERNAME/spark:v2.4.5 .
  ```

  This will give you `your_username/spark:v2.4.5-s3` with AWS S3 SDK libraries.


#### Build your own Spark

If you would like to build your own spark, you can use profile `-Phadoop-3.1` or `-Phadoop-2.7`. Just remember to choose right `hadoop-aws` and `aws-java-sdk-bundle` version.

Once you build your own Spark, create a distribution and all rest steps is exact same as above steps.

Check appendix A for more details to build Apache Spark from source.

## Build benchamrk project

### Build Dependencies
- [spark-sql-perf](https://github.com/databricks/spark-sql-perf)

Latest version in maven central repo is 0.3.2 which is too old, we need to build a new libary from source. This library `spark-sql-perf_2.11-0.5.1-SNAPSHOT.jar` has been added as a dependency in `benchmark/libs` if you want to skip it.

```
git clone https://github.com/databricks/spark-sql-perf
cd spark-sql-perf
sbt +package
```

```
cp target/scala-2.11/spark-sql-perf_2.11-0.5.1-SNAPSHOT.jar <your-code-path>/eks-spark-benchmark/benchmark/libs
```

### Build benchmark utility

```
$ sbt assembly
```

### Build container image

```
IMAGE_TAG=v0.1-$(date +'%Y%m%d')
docker build -t $DOCKERHUB_USERNAME/spark-benchmark:$IMAGE_TAG .
```

If you like to build based on a different spark base image.

```
docker build -t $DOCKERHUB_USERNAME/spark-benchmark:$IMAGE_TAG --build-arg SPARK_BASE_IMAGE=$DOCKERHUB_USERNAME/spark:v2.4.5-s3 .
```

You can push image to dockerhub and then use image `$DOCKERHUB_USERNAME/spark-benchmark:$IMAGE_TAG` to replace image `seedjeffwan/spark:v2.4.5-examples` in the examples.

> Note: If you build Spark source using Scala 2.12, please copy `scala-2.12/spark-sql-perf_2.12-0.5.1-SNAPSHOT.jar` and update scale version in benchmark project `build.sbt` and `Dockerfile`. Make sure you use scala-2.12 in all the application to match your Spark Scala version.


## Install Spark-Operator

We highly recommend you use [spark-operator](https://github.com/GoogleCloudPlatform/spark-on-k8s-operator) to manage spark applications. Please check [installation guidance](https://github.com/GoogleCloudPlatform/spark-on-k8s-operator#installation) to install spark-operator.

To setup helm,

```
# Create tiller Service Account
cat <<EOF | kubectl apply --filename -
apiVersion: v1
kind: ServiceAccount
metadata:
  name: tiller
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: tiller
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
  - kind: ServiceAccount
    name: tiller
    namespace: kube-system
EOF

# Init Helm with tiller
helm init --service-account tiller
```

Here's an example to use `seedjeffwan/spark-operator:v2.4.5` as your spark operator image.
```
helm install incubator/sparkoperator --namespace spark-operator --set enableWebhook=true --set sparkJobNamespace=default --set operatorImageName=seedjeffwan/spark-operator --set operatorVersion=v2.4.5
```

Create Spark service account

```
kubectl create serviceaccount spark
kubectl create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark --namespace=default
```

You can use service account `spark` in your driver pod.

## Run Benchmark

### Generate the TCP-DS data

```shell
kubectl apply -f examples/tpcds-data-generation.yaml
```

### Run TPC-DS queries

```shell
kubectl apply -f examples/tpcds-benchmark.yaml
```

> Note: We use 1G dataset in the yaml examples, if you'd like to change to 100G or 1T, don't forget to change data num partitions as well. Executors resources can be changed correspondingly.

## Appendix A. Build Spark distribution from Source code.

To create a Spark distribution like those distributed by the [Spark Downloads](https://spark.apache.org/downloads.html) page, and that is laid out so as to be runnable, use `./dev/make-distribution.sh` in the project root directory. It can be configured with Maven profile settings and so on like the direct Maven build. Example:

```
./dev/make-distribution.sh --name custom-spark --pip --r --tgz -Psparkr -Phadoop-3.1 -Phive -Phive-thriftserver -Pmesos -Pyarn -Pkubernetes
```

This will build Spark distribution along with Python pip and R packages. For more information on usage, run `./dev/make-distribution.sh --help`