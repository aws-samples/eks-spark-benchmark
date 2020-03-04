## Important Performance fix

Most of the performance of Spark operations is mainly consumed in the shuffle phase, because it contains a large number of disk IO, serialization, network data transmission and other operations. There're a few performance improvement on the disk that spark scratch space uses.

### Migrate temporary storage for Spark jobs to EmptyDir

https://github.com/apache-spark-on-k8s/spark/issues/439
https://github.com/apache-spark-on-k8s/spark/pull/486

Spark jobs use dirs inside the driver and executor pods for storing temporary files. For instance, the work dirs for the Spark driver and executors use dirs inside the pods.

These dirs are within the docker storage backend, which can be slow due to its copy-on-write(CoW) overhead. Many of the storage backends implement block level CoW. Each small write will incur copy of the entire block. The overhead can become very high if the files are updated by many small writes. It is recommended to avoid using docker storage backend for such use cases. From [Docker storage drivers](https://docs.docker.com/storage/storagedriver/select-storage-driver/):

> Ideally, very little data is written to a container’s writable layer, and you use Docker volumes to write data.

This might prove to be important for performance, especially in shuffle-heavy computations where the executors perform a large amount of disk I/O. We only provision these volumes in static allocation mode without using the shuffle service because using a shuffle service requires mounting hostPath volumes, instead.


### [SPARK-25262] Support tmpfs for local dirs in k8s

Spark creates a Kubernetes emptyDir volume for each directory specified in `spark.local.dirs`. As noted in the Kubernetes documentation this will be backed by the node storage (https://kubernetes.io/docs/concepts/storage/volumes/#emptydir). In some compute environments this may be extremely undesirable. For example with diskless compute resources the node storage will likely be a non-performant remote mounted disk, often with limited capacity. For such environments it would likely be better to set medium: Memory on the volume per the K8S documentation to use a tmpfs volume instead.

```shell
# spark-submit example
./bin/spark-submit \
--master k8s://https://YOUR_EKS_MASTER_URI.sk1.us-west-2.eks.amazonaws.com \
--deploy-mode cluster \
--name spark-pi \
--class org.apache.spark.examples.SparkPi  \
--conf spark.executor.instances=2 \
--conf spark.kubernetes.local.dirs.tmpfs=true \
--conf spark.kubernetes.container.image=seedjeffwan/spark:v2.4.5 \
--conf spark.kubernetes.driver.pod.name=spark-pi \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark local:///opt/spark/jars/spark-examples_2.11-2.4.5.jar
```

```yaml
# spark-operator example
spec:
  sparkConf:
    "spark.kubernetes.local.dirs.tmpfs": "true"
    ...
```

### [SPARK-28042] [SPARK-27499] Support mapping spark.local.dir to hostPath volume

Currently, the k8s executor builder mount `spark.local.dir` as emptyDir or memory, it should satisfy some small workload, while in some heavily workload like TPCDS, both of them can have some problem, such as pods are evicted due to disk pressure when using emptyDir, and OOM when using tmpfs. This story expose hostPath volume to `spark.local.dir` and it can leverage local high performance disk for shuffle.

> Note: Instances with SSD, NVMe are highly recommended.

```shell
# spark-submit example

./bin/spark-submit \
--master k8s://https://YOUR_EKS_MASTER_URI.sk1.us-west-2.eks.amazonaws.com \
--deploy-mode cluster \
--name spark-pi \
--class org.apache.spark.examples.SparkPi  \
--conf spark.executor.instances=2 \
--conf spark.local.dir=/tmp/mnt-1 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.mount.path=/tmp/mnt-1 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.options.path=/tmp/mnt-1 \
--conf spark.kubernetes.container.image=seedjeffwan/spark:v2.4.5 \
--conf spark.kubernetes.driver.pod.name=spark-pi \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark local:///opt/spark/jars/spark-examples_2.11-2.4.5.jar
```

```yaml
# spark-operator example
spec:
  ...
  mainApplicationFile: local:///opt/spark/examples/jars/eks-spark-examples-assembly-1.0.jar
  volumes:
    - name: "spark-local-dir-1"
      hostPath:
        path: "/tmp/spark-local-dir"
  executor:
    instances: 10
    cores: 2
    ....
    volumeMounts:
      - name: "spark-local-dir-1"
        mountPath: "/tmp/spark-local-dir"
```

### Use multiple disk for scratch space

kubelet work directory can only be mounted on one disk, so that the spark scratch space only use ONE disk.
Single disk I/O maybe the performance bottleneck and you can add more disks for scrach space.

```shell
# spark-submit example

./bin/spark-submit \
--master k8s://https://YOUR_EKS_MASTER_URI.sk1.us-west-2.eks.amazonaws.com \
--deploy-mode cluster \
--name spark-pi \
--class org.apache.spark.examples.SparkPi  \
--conf spark.executor.instances=2 \
--conf spark.local.dir=/tmp/mnt-1,/tmp/mnt-2,/tmp/mnt-3 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.mount.path=/tmp/mnt-1 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.options.path=/tmp/mnt-1 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-2.mount.path=/tmp/mnt-2 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-2.options.path=/tmp/mnt-2 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-3.mount.path=/tmp/mnt-3 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-3.options.path=/tmp/mnt-3 \
--conf spark.kubernetes.container.image=seedjeffwan/spark:v2.4.5 \
--conf spark.kubernetes.driver.pod.name=spark-pi \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark local:///opt/spark/jars/spark-examples_2.11-2.4.5.jar
```

```yaml
# spark-operator example

spec:
  ...
  volumes:
    - name: "spark-local-dir-1"
      hostPath:
        path: "/tmp/mnt-1"
    - name: "spark-local-dir-2"
      hostPath:
        path: "/tmp/mnt-2"
    - name: "spark-local-dir-3"
      hostPath:
        path: "/tmp/mnt-3"
  driver:
    ...
  executor:
    cores: 1
    ...
    volumeMounts:
      - name: "spark-local-dir-1"
        mountPath: "/tmp/mnt-1"
      - name: "spark-local-dir-2"
        mountPath: "/tmp/mnt-2"
      - name: "spark-local-dir-3"
        mountPath: "/tmp/mnt-3"
```