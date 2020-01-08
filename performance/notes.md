


datagen is cpu intensive but not memory. but we allocate more memory to machine
```
NAME                                           CPU(cores)   CPU%   MEMORY(bytes)   MEMORY%
ip-192-168-13-39.us-west-2.compute.internal    5700m        71%    12822Mi         20%
ip-192-168-13-53.us-west-2.compute.internal    3649m        45%    8672Mi          14%
ip-192-168-14-217.us-west-2.compute.internal   5236m        65%    12458Mi         20%
ip-192-168-19-21.us-west-2.compute.internal    3577m        44%    8650Mi          14%
ip-192-168-25-234.us-west-2.compute.internal   1919m        23%    5304Mi          8%
ip-192-168-31-170.us-west-2.compute.internal   3638m        45%    8608Mi          14%
ip-192-168-31-252.us-west-2.compute.internal   5258m        65%    12469Mi         20%
ip-192-168-31-84.us-west-2.compute.internal    5279m        65%    12836Mi         20%
ip-192-168-37-94.us-west-2.compute.internal    5235m        65%    12566Mi         20%
ip-192-168-39-19.us-west-2.compute.internal    5690m        71%    12369Mi         20%
ip-192-168-40-247.us-west-2.compute.internal   3630m        45%    8608Mi          14%
ip-192-168-40-81.us-west-2.compute.internal    3573m        44%    8626Mi          14%
ip-192-168-41-104.us-west-2.compute.internal   3554m        44%    8618Mi          14%
ip-192-168-41-143.us-west-2.compute.internal   5489m        68%    12472Mi         20%
ip-192-168-42-45.us-west-2.compute.internal    3593m        44%    9075Mi          14%
ip-192-168-45-155.us-west-2.compute.internal   5376m        67%    12480Mi         20%
ip-192-168-45-172.us-west-2.compute.internal   5437m        67%    12415Mi         20%
ip-192-168-47-142.us-west-2.compute.internal   3573m        44%    8565Mi          13%
ip-192-168-7-138.us-west-2.compute.internal    5798m        72%    12392Mi         20%
ip-192-168-7-141.us-west-2.compute.internal    5247m        65%    12583Mi         20%
```


100G  100 partition. DataGen

```
19/09/07 23:37:25 WARN TaskSetManager: Lost task 13.0 in stage 4.0 (TID 413, 192.168.37.252, executor 37): org.apache.spark.SparkException: Task failed while writing rows.
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$.org$apache$spark$sql$execution$datasources$FileFormatWriter$$executeTask(FileFormatWriter.scala:257)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$$anonfun$write$1.apply(FileFormatWriter.scala:170)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$$anonfun$write$1.apply(FileFormatWriter.scala:169)
	at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:90)
	at org.apache.spark.scheduler.Task.run(Task.scala:123)
	at org.apache.spark.executor.Executor$TaskRunner$$anonfun$10.apply(Executor.scala:408)
	at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:1360)
	at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:414)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.io.IOException: Failed to rename S3AFileStatus{path=s3a://spark-k8s-data/TPCDS-TEST-100G/store_returns/_temporary/0/_temporary/attempt_20190907233716_0004_m_000013_413/part-00013-e11a2fdb-2085-49c0-b0cc-8f85b639cb4e-c000.snappy.parquet; isDirectory=false; length=16213032; replication=1; blocksize=33554432; modification_time=1567899445000; access_time=0; owner=root; group=root; permission=rw-rw-rw-; isSymlink=false; hasAcl=false; isEncrypted=false; isErasureCoded=false} isEmptyDirectory=FALSE to s3a://spark-k8s-data/TPCDS-TEST-100G/store_returns/part-00013-e11a2fdb-2085-49c0-b0cc-8f85b639cb4e-c000.snappy.parquet
	at org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.mergePaths(FileOutputCommitter.java:473)
	at org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.mergePaths(FileOutputCommitter.java:486)
	at org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.commitTask(FileOutputCommitter.java:597)
	at org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.commitTask(FileOutputCommitter.java:560)
	at org.apache.spark.mapred.SparkHadoopMapRedUtil$.performCommit$1(SparkHadoopMapRedUtil.scala:50)
	at org.apache.spark.mapred.SparkHadoopMapRedUtil$.commitTask(SparkHadoopMapRedUtil.scala:77)
	at org.apache.spark.internal.io.HadoopMapReduceCommitProtocol.commitTask(HadoopMapReduceCommitProtocol.scala:225)
	at org.apache.spark.sql.execution.datasources.FileFormatDataWriter.commit(FileFormatDataWriter.scala:78)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$$anonfun$org$apache$spark$sql$execution$datasources$FileFormatWriter$$executeTask$3.apply(FileFormatWriter.scala:247)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$$anonfun$org$apache$spark$sql$execution$datasources$FileFormatWriter$$executeTask$3.apply(FileFormatWriter.scala:242)
	at org.apache.spark.util.Utils$.tryWithSafeFinallyAndFailureCallbacks(Utils.scala:1394)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$.org$apache$spark$sql$execution$datasources$FileFormatWriter$$executeTask(FileFormatWriter.scala:248)
	... 10 more


```


```
19/09/07 23:38:45 INFO TPCDSTables: Generating table web_returns in database to s3a://spark-k8s-data/TPCDS-TEST-100G/web_returns with save mode Overwrite.
19/09/07 23:38:51 WARN TaskSetManager: Lost task 3.0 in stage 6.0 (TID 604, 192.168.10.48, executor 47): org.apache.spark.SparkException: Task failed while writing rows.
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$.org$apache$spark$sql$execution$datasources$FileFormatWriter$$executeTask(FileFormatWriter.scala:257)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$$anonfun$write$1.apply(FileFormatWriter.scala:170)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$$anonfun$write$1.apply(FileFormatWriter.scala:169)
	at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:90)
	at org.apache.spark.scheduler.Task.run(Task.scala:123)
	at org.apache.spark.executor.Executor$TaskRunner$$anonfun$10.apply(Executor.scala:408)
	at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:1360)
	at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:414)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.io.IOException: Failed to rename S3AFileStatus{path=s3a://spark-k8s-data/TPCDS-TEST-100G/web_returns/_temporary/0/_temporary/attempt_20190907233846_0006_m_000003_604/part-00003-a8a25ee1-b7e1-433d-a780-ef121dda00f6-c000.snappy.parquet; isDirectory=false; length=5500504; replication=1; blocksize=33554432; modification_time=1567899531000; access_time=0; owner=root; group=root; permission=rw-rw-rw-; isSymlink=false; hasAcl=false; isEncrypted=false; isErasureCoded=false} isEmptyDirectory=FALSE to s3a://spark-k8s-data/TPCDS-TEST-100G/web_returns/part-00003-a8a25ee1-b7e1-433d-a780-ef121dda00f6-c000.snappy.parquet
	at org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.mergePaths(FileOutputCommitter.java:473)
	at org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.mergePaths(FileOutputCommitter.java:486)
	at org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.commitTask(FileOutputCommitter.java:597)
	at org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter.commitTask(FileOutputCommitter.java:560)
	at org.apache.spark.mapred.SparkHadoopMapRedUtil$.performCommit$1(SparkHadoopMapRedUtil.scala:50)
	at org.apache.spark.mapred.SparkHadoopMapRedUtil$.commitTask(SparkHadoopMapRedUtil.scala:77)
	at org.apache.spark.internal.io.HadoopMapReduceCommitProtocol.commitTask(HadoopMapReduceCommitProtocol.scala:225)
	at org.apache.spark.sql.execution.datasources.FileFormatDataWriter.commit(FileFormatDataWriter.scala:78)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$$anonfun$org$apache$spark$sql$execution$datasources$FileFormatWriter$$executeTask$3.apply(FileFormatWriter.scala:247)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$$anonfun$org$apache$spark$sql$execution$datasources$FileFormatWriter$$executeTask$3.apply(FileFormatWriter.scala:242)
	at org.apache.spark.util.Utils$.tryWithSafeFinallyAndFailureCallbacks(Utils.scala:1394)
	at org.apache.spark.sql.execution.datasources.FileFormatWriter$.org$apache$spark$sql$execution$datasources$FileFormatWriter$$executeTask(FileFormatWriter.scala:248)
	... 10 more


```


1g benchmark
  driver:
    cores: 4
    coreLimit: "4096m"
    memory: "8000m"
    serviceAccount: spark
  executor:
    instances: 20
    cores: 1
    memory: "8000m"
    memoryOverhead: "2g"
  restartPolicy:
    type: Never


```
19/09/08 17:33:43 WARN TaskSetManager: Lost task 0.1 in stage 50.0 (TID 1998, 192.168.0.99, executor 23): TaskKilled (another attempt succeeded)
19/09/08 17:33:48 WARN TaskSetManager: Lost task 5.0 in stage 60.0 (TID 2078, 192.168.39.235, executor 57): TaskKilled (another attempt succeeded)
19/09/08 17:33:49 WARN TaskSetManager: Lost task 2.0 in stage 58.0 (TID 2064, 192.168.22.62, executor 75): TaskKilled (another attempt succeeded)
19/09/08 17:33:49 WARN TaskSetManager: Lost task 5.1 in stage 58.0 (TID 2128, 192.168.3.117, executor 55): TaskKilled (another attempt succeeded)
19/09/08 17:33:50 WARN TaskSetManager: Lost task 15.1 in stage 54.0 (TID 2151, 192.168.3.154, executor 56): TaskKilled (another attempt succeeded)
19/09/08 17:33:50 WARN TaskSetManager: Lost task 10.1 in stage 52.0 (TID 2150, 192.168.36.103, executor 7): TaskKilled (another attempt succeeded)
19/09/08 17:33:50 WARN TaskSetManager: Lost task 26.1 in stage 56.0 (TID 2165, 192.168.48.239, executor 67): TaskKilled (another attempt succeeded)
19/09/08 17:33:50 WARN TaskSetManager: Lost task 4.0 in stage 54.0 (TID 2014, 192.168.48.249, executor 64): TaskKilled (another attempt succeeded)
19/09/08 17:33:50 WARN TaskSetManager: Lost task 9.0 in stage 56.0 (TID 2042, 192.168.37.213, executor 30): TaskKilled (another attempt succeeded)
19/09/08 17:33:50 WARN TaskSetManager: Lost task 11.1 in stage 60.0 (TID 2126, 192.168.47.148, executor 62): TaskKilled (another attempt succeeded)
19/09/08 17:33:51 WARN TaskSetManager: Lost task 5.1 in stage 52.0 (TID 2149, 192.168.23.123, executor 66): TaskKilled (another attempt succeeded)
19/09/08 17:33:52 WARN TaskSetManager: Lost task 11.1 in stage 54.0 (TID 2153, 192.168.52.248, executor 26): TaskKilled (another attempt succeeded)
19/09/08 17:33:52 WARN TaskSetManager: Lost task 14.1 in stage 62.0 (TID 2127, 192.168.59.7, executor 77): TaskKilled (another attempt succeeded)
19/09/08 17:33:55 WARN TaskSetManager: Lost task 14.0 in stage 56.0 (TID 2047, 192.168.61.233, executor 31): TaskKilled (another attempt succeeded)

```

If a task appears to be taking an unusually long time to complete, Spark may launch extra duplicate copies of that task in case they can complete sooner. This is referred to as speculation or speculative execution. If one copy succeeds, the others can be killed.

See the parameters starting with spark.speculation here: https://spark.apache.org/docs/latest/configuration.html

So it's reasonable those tasks are killed.


Why do we need `spark.sql.crossjoin.enabled=true` ?


The TPCDS query set benchmarks have queries that contain CROSS JOINS and unless you explicitly write CROSS JOIN or dynamically set Spark's default property to true Spark.conf.set("spark.sql.crossJoin.enabled", true) you will run into an exception error.

The error appears on TPCDS queries 28,61, 88, and 90 becuase the original query syntax from Transaction Processing Committee (TPC) contains commas and Spark's default join operation is an inner join. My team has also decided to use CROSS JOIN in lieu of changing Spark's default properties.
https://stackoverflow.com/questions/38999140/spark-sql-crossjoin-enabled-for-spark-2-x/39000050#39000050


Have a try `spark.sql.crossjoin.enabled=false`. just on sql list


1. Why stages are skipped?

Typically it means that data has been fetched from cache and there was no need to re-execute given stage. It is consistent with your DAG which shows that the next stage requires shuffling (reduceByKey). Whenever there is shuffling involved Spark automatically caches generated data:
https://stackoverflow.com/questions/34580662/what-does-stage-skipped-mean-in-apache-spark-web-ui

2. If all exectutor joins and then spark driver start real work? -> Give Cluster Autoscaler opportunity?
num_executors <200 (small job)  <1000 (big job)
executor_core <3 (disk, network problem)
executor_mem < 15, if there's broadcast



1t - only once

Disk Eviction - frequently

19/09/08 20:09:39 ERROR TaskSchedulerImpl: Lost executor 63 on 192.168.35.237:
The executor with id 63 exited with exit code -1.
The API gave the following brief reason: Evicted
The API gave the following message: The node was low on resource: ephemeral-storage. Container executor was using 8164Ki, which exceeds its request of 0.
The API gave the following container statuses:



19/09/08 20:09:39 WARN TaskSetManager: Lost task 103.0 in stage 884.0 (TID 212570, 192.168.35.237, executor 63): ExecutorLostFailure (executor 63 exited caused by one of the running tasks) Reason:
The executor with id 63 exited with exit code -1.
The API gave the following brief reason: Evicted
The API gave the following message: The node was low on resource: ephemeral-storage. Container executor was using 8164Ki, which exceeds its request of 0.
The API gave the following container statuses:



19/09/08 20:09:48 WARN TransportChannelHandler: Exception in connection from /192.168.35.237:60686
java.io.IOException: Connection reset by peer
	at sun.nio.ch.FileDispatcherImpl.read0(Native Method)
	at sun.nio.ch.SocketDispatcher.read(SocketDispatcher.java:39)
	at sun.nio.ch.IOUtil.readIntoNativeBuffer(IOUtil.java:223)
	at sun.nio.ch.IOUtil.read(IOUtil.java:192)
	at sun.nio.ch.SocketChannelImpl.read(SocketChannelImpl.java:380)
	at io.netty.buffer.PooledUnsafeDirectByteBuf.setBytes(PooledUnsafeDirectByteBuf.java:288)
	at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1106)
	at io.netty.channel.socket.nio.NioSocketChannel.doReadBytes(NioSocketChannel.java:343)
	at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:123)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:645)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:580)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:497)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:459)
	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:858)
	at io.netty.util.concurrent.DefaultThreadFactory$DefaultRunnableDecorator.run(DefaultThreadFactory.java:138)
	at java.lang.Thread.run(Thread.java:748)
19/09/08 20:12:46 WARN TaskSetManager: Lost task 103.1 in stage 884.0 (TID 212667, 192.168.12.97, executor 47): FetchFailed(BlockManagerId(63, 192.168.35.237, 36285, None), shuffleId=440, mapId=51, reduceId=103, message=
org.apache.spark.shuffle.FetchFailedException: Failed to connect to /192.168.35.237:36285
	at org.apache.spark.storage.ShuffleBlockFetcherIterator.throwFetchFailedException(ShuffleBlockFetcherIterator.scala:554)
	at org.apache.spark.storage.ShuffleBlockFetcherIterator.next(ShuffleBlockFetcherIterator.scala:485)
	at org.apache.spark.storage.ShuffleBlockFetcherIterator.next(ShuffleBlockFetcherIterator.scala:64)
	at scala.collection.Iterator$$anon$12.nextCur(Iterator.scala:435)
	at scala.collection.Iterator$$anon$12.hasNext(Iterator.scala:441)
	at scala.collection.Iterator$$anon$11.hasNext(Iterator.scala:409)
	at org.apache.spark.util.CompletionIterator.hasNext(CompletionIterator.scala:31)
	at org.apache.spark.InterruptibleIterator.hasNext(InterruptibleIterator.scala:37)
	at scala.collection.Iterator$$anon$11.hasNext(Iterator.scala:409)
	at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIteratorForCodegenStage6.sort_addToSorter_0$(Unknown Source)
	at org.apache.spark.sql.catalyst.expressions.GeneratedClass$GeneratedIteratorForCodegenStage6.processNext(Unknown Source)
	at org.apache.spark.sql.execution.BufferedRowIterator.hasNext(BufferedRowIterator.java:43)
	at org.apache.spark.sql.execution.WholeStageCodegenExec$$anonfun$13$$anon$1.hasNext(WholeStageCodegenExec.scala:636)
	at org.apache.spark.sql.execution.window.WindowExec$$anonfun$11$$anon$1.fetchNextRow(WindowExec.scala:314)
	at org.apache.spark.sql.execution.window.WindowExec$$anonfun$11$$anon$1.<init>(WindowExec.scala:323)
	at org.apache.spark.sql.execution.window.WindowExec$$anonfun$11.apply(WindowExec.scala:303)
	at org.apache.spark.sql.execution.window.WindowExec$$anonfun$11.apply(WindowExec.scala:302)
	at org.apache.spark.rdd.RDD$$anonfun$mapPartitions$1$$anonfun$apply$23.apply(RDD.scala:801)
	at org.apache.spark.rdd.RDD$$anonfun$mapPartitions$1$$anonfun$apply$23.apply(RDD.scala:801)
	at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:52)
	at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:324)
	at org.apache.spark.rdd.RDD.iterator(RDD.scala:288)
	at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:52)
	at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:324)
	at org.apache.spark.rdd.RDD.iterator(RDD.scala:288)
	at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:52)
	at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:324)
	at org.apache.spark.rdd.RDD.iterator(RDD.scala:288)
	at org.apache.spark.rdd.MapPartitionsRDD.compute(MapPartitionsRDD.scala:52)
	at org.apache.spark.rdd.RDD.computeOrReadCheckpoint(RDD.scala:324)
	at org.apache.spark.rdd.RDD.iterator(RDD.scala:288)
	at org.apache.spark.scheduler.ResultTask.runTask(ResultTask.scala:90)
	at org.apache.spark.scheduler.Task.run(Task.scala:123)
	at org.apache.spark.executor.Executor$TaskRunner$$anonfun$10.apply(Executor.scala:408)
	at org.apache.spark.util.Utils$.tryWithSafeFinally(Utils.scala:1360)
	at org.apache.spark.executor.Executor$TaskRunner.run(Executor.scala:414)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.io.IOException: Failed to connect to /192.168.35.237:36285
	at org.apache.spark.network.client.TransportClientFactory.createClient(TransportClientFactory.java:245)
	at org.apache.spark.network.client.TransportClientFactory.createClient(TransportClientFactory.java:187)
	at org.apache.spark.network.netty.NettyBlockTransferService$$anon$2.createAndStart(NettyBlockTransferService.scala:114)
	at org.apache.spark.network.shuffle.RetryingBlockFetcher.fetchAllOutstanding(RetryingBlockFetcher.java:141)
	at org.apache.spark.network.shuffle.RetryingBlockFetcher.lambda$initiateRetry$0(RetryingBlockFetcher.java:169)
	at java.util.concurrent.Executors$RunnableAdapter.call(Executors.java:511)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at io.netty.util.concurrent.DefaultThreadFactory$DefaultRunnableDecorator.run(DefaultThreadFactory.java:138)
	... 1 more
Caused by: io.netty.channel.AbstractChannel$AnnotatedNoRouteToHostException: Host is unreachable: /192.168.35.237:36285
	at sun.nio.ch.SocketChannelImpl.checkConnect(Native Method)
	at sun.nio.ch.SocketChannelImpl.finishConnect(SocketChannelImpl.java:717)
	at io.netty.channel.socket.nio.NioSocketChannel.doFinishConnect(NioSocketChannel.java:323)
	at io.netty.channel.nio.AbstractNioChannel$AbstractNioUnsafe.finishConnect(AbstractNioChannel.java:340)
	at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:633)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:580)
	at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:497)
	at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:459)
	at io.netty.util.concurrent.SingleThreadEventExecutor$5.run(SingleThreadEventExecutor.java:858)
	... 2 more
Caused by: java.net.NoRouteToHostException: Host is unreachable
	... 11 more



  Events:
  Type     Reason                Age                   From                                                  Message
  ----     ------                ----                  ----                                                  -------
  Normal   NodeHasDiskPressure   7m27s (x2 over 157m)  kubelet, ip-192-168-39-19.us-west-2.compute.internal  Node ip-192-168-39-19.us-west-2.compute.internal status is now: NodeHasDiskPressure
  Warning  FreeDiskSpaceFailed   6m48s                 kubelet, ip-192-168-39-19.us-west-2.compute.internal  failed to garbage collect required amount of images. Wanted to free 4277782937 bytes, but freed 0 bytes
  Warning  EvictionThresholdMet  4m16s (x5 over 157m)  kubelet, ip-192-168-39-19.us-west-2.compute.internal  Attempting to reclaim ephemeral-storage



  1. Machine only has 20Gi ephemeral-storage. If I use 5Gi as default value, 1 machine can only fit 4 containers.

  Events:
  Type     Reason            Age                 From               Message
  ----     ------            ----                ----               -------
  Warning  FailedScheduling  44s (x3 over 109s)  default-scheduler  0/20 nodes are available: 1 Insufficient cpu, 19 Insufficient ephemeral-storage.

```
apiVersion: v1
kind: LimitRange
metadata:
  name: ephemeral-storage-limit-range
spec:
  limits:
  - default:
      ephemeral-storage: 5Gi
    defaultRequest:
      ephemeral-storage: 3Gi
    type: Container
```

Based on previous statistics, we have 5-6 containers per node, then we change default to 3GiB

Question. How to increase size ephemeral-storage on node. Why there's only 20G available?

https://stackoverflow.com/questions/53559461/how-to-set-ephemeral-storage-in-spark-with-kubernetes


Kubernetes version 1.8 introduces a new resource, ephemeral-storage for managing local ephemeral storage. In each Kubernetes node, kubelet’s root directory (/var/lib/kubelet by default) and log directory (/var/log) are stored on the root partition of the node. This partition is also shared and consumed by Pods via emptyDir volumes, container logs, image layers and container writable layers.

This partition is “ephemeral” and applications cannot expect any performance SLAs (Disk IOPS for example) from this partition. Local ephemeral storage management only applies for the root partition; the optional partition for image layer and writable layer is out of scope.


Seems we don't have failures for 10T data after we increase ephemeral-storage.


We have some logs like this later.
```
19/09/08 23:13:47 ERROR TaskSchedulerImpl: Lost executor 58 on 192.168.53.62:
The executor with id 58 exited with exit code -1.
The API gave the following brief reason: Evicted
The API gave the following message: The node was low on resource: ephemeral-storage.
The API gave the following container statuses:


19/09/08 23:13:47 ERROR TaskSchedulerImpl: Lost executor 74 on 192.168.15.203:
The executor with id 74 exited with exit code -1.
The API gave the following brief reason: Evicted
The API gave the following message: The node was low on resource: ephemeral-storage.
The API gave the following container statuses:
```
