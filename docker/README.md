### Build Spark Container Image with S3 support

Build Spark Container Image and choose right version. You can also build your hadoop free Spark

```
docker build -t container_tag --build-arg BASE_IMAGE=seedjeffwan/spark:2.4.5-SNAPSHOT --build-arg HADOOP_AWS_VERSION=3.2.0 --build-arg AWS_JAVA_SDK_VERSION=1.11.375 .
```