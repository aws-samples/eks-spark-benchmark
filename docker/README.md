## Add S3 support using prebuilt Spark binary

We suggest to use `seedjeffwan/spark:v2.4.5` direclty and it already hadoop-3.1 compatible s3 client and performance improvements for Spark on Kubernetes.

### Build Spark Container Image with S3 support

Build Spark Container Image and choose right version. Override `BASE_IMAGE`, `HADOOP_AWS_VERSION` and `AWS_JAVA_SDK_VERSION`.

```
docker build -t container_tag --build-arg BASE_IMAGE=<your_spark_image> --build-arg HADOOP_AWS_VERSION=3.1.0 --build-arg AWS_JAVA_SDK_VERSION=1.11.271 .
```

> Note: We use `seedjeffwan/spark:v2.4.5` as base image in Dockerfile. `hadoop-aws-3.1.0.jar` and `aws-java-sdk-bundle-1.11.271.jar` already exist in the image. That means if you bring imcompatible version of aws sdk, it's possible to break the application. If you don't plan to use `seedjeffwan/spark:v2.4.5`, please override BASE_IMAGE with your own spark base image.