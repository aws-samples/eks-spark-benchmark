### Gang Scheduling

With gang-scheduling, a Spark application can only be scheduled if there are enough resources for all the pods of the job. Comparing to pod-by-pod scheduling by default Kubernetes scheduler, gang-scheduling makes sure it schedule spark job-by-job.

Gang scheduling ensure that Kubernetes never luanches partial applications. This help resolve resource dead locks issues from different jobs.

For example, if a job requiring N pods is requested and there are only enough resources to schedule N-2 pods, then N pods of the job won't be scheduled until more resources is released to fit N pods. At the same time, if there're jobs request less or equal than N-2 pods, it can be scheduled.

1. Enable Batch Scheduler in Spark-Operator

```
$ helm install incubator/sparkoperator --namespace spark-operator --set enableBatchScheduler=true
```

1. Install [Volcano](https://volcano.sh/)

Follow [instruction](https://github.com/volcano-sh/volcano#install-with-yaml-files) to install volcano.

1. Submit job using volcano scheduler

```
apiVersion: "sparkoperator.k8s.io/v1beta2"
kind: SparkApplication
metadata:
  name: spark-pi
  namespace: default
spec:
  type: Scala
  mode: cluster
  image: "gcr.io/spark-operator/spark:v2.4.4"
  imagePullPolicy: Always
  mainClass: org.apache.spark.examples.SparkPi
  mainApplicationFile: "local:///opt/spark/examples/jars/spark-examples_2.11-2.4.4.jar"
  sparkVersion: "2.4.4"
  batchScheduler: "volcano"   #Note: the batch scheduler name must be specified with `volcano`
  ....
```

When running, the Pods Events can be used to verify that whether the pods have been scheduled via Volcano.

```
Type    Reason     Age   From                          Message
----    ------     ----  ----                          -------
Normal  Scheduled  23s   volcano                       Successfully assigned default/spark-pi-driver to ip-192-168-3-136.us-west-2.compute.internal
```

By default, Gang Scheduling is enabled in volcano and it will caculate all resources requested by driver and exectutors. If cluster doesn't have enough resources, it will not schedule the job.

### Zone-Awareness Scheduling

Basically, in order to get higher availability and mitigate the risk of not having enough capacity in single AZ, users create EKS worker node groups across multiple availability zones (AZs). For spark applications, Shuffling is a process of redistributing data across partitions that may cause moving data across JVM processes or even over the wire (between executors in different zones). There's network overhead between different AZs within a region and extra cost for cross-zone data transfer. It's better for scheduler to be availability-zone aware, scheduler can fit as many executors for a single job in one AZ as possible.

https://github.com/volcano-sh/volcano/issues/447


### Task Topology + Advanced Binpacking

Kubernetes default scheduler tries to spread pods across the nodes. For Spark applications, this brings some overhead because increase network latency and shuffle remote fetch will take longer time. In order to reduce network overhead and resource fragmentation, we can use task topology and binpack to schedule Spark applications and make sure executors can be scheduled as close as possible.

https://github.com/volcano-sh/volcano/issues/272

> Note: This only work for some specific use cases. Put more executors on one node may easily hit network throught limit or IOPS.