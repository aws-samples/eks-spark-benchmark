## Benchmark Configuration

### Cluster Spec

|   Spec    |  EMR  |    EKS    |
|-----------|-------|-----------|
|   Master  |  1	  |     N/A   |
|  Executor	|  3	  |     4     |
| Committer	|  S3A  |    S3A    |
|  TPC-DS   |  1T   |     1T    |


| Instance 	  | vCPU 	| Mem (GiB) | Storage 	| Networking Performance (Gbps)	|
|-----------	|-------|----------	|----------	|-------------------------------|
| r4.2xlarge	|  8	  |     61   	|  EBS-Only |         Up to 10     	        |


### Spark application configurations

```shell
--driver-cores 4
--driver-memory 8G
--executor-cores 2
--executor-memory 8G
--num-executors 10
--conf spark.executor.memoryOverhead=2G
```

```
# Cloud specific
"spark.speculation": "true"
"spark.speculation.multiplier": "3"
"spark.speculation.quantile": "0.9"

# TPCDs Specific
"spark.sql.broadcastTimeout": "7200"
"spark.sql.crossJoin.enabled": "true"
"spark.sql.parquet.mergeSchema": "false"
"spark.sql.parquet.filterPushdown": "true"

# S3 credential
"spark.hadoop.fs.s3a.aws.credentials.provider": "com.amazonaws.auth.InstanceProfileCredentialsProvider"

# S3 Specific config
# We need it to speed up uploads, and outputcommiter/parquet to have consistent writes due to speculation
"spark.hadoop.fs.s3a.connection.timeout": "1200000"
"spark.hadoop.fs.s3a.path.style.access": "true"
"spark.hadoop.fs.s3a.connection.maximum": "200"
"spark.hadoop.fs.s3a.fast.upload": "true"

# S3 Committer
"spark.hadoop.mapreduce.outputcommitter.factory.scheme.s3a": "org.apache.hadoop.fs.s3a.commit.S3ACommitterFactory"
"spark.hadoop.fs.s3a.committer.name": "directory"
"spark.hadoop.fs.s3a.committer.staging.conflict-mode": "append"
```

## EMR

### EMR Cluster CI Export

```
aws emr create-cluster --termination-protected --applications Name=Hadoop Name=Hive Name=Pig Name=Hue Name=Spark --ec2-attributes '{"KeyName":"aws-key","InstanceProfile":"EMR_EC2_DefaultRole","SubnetId":"subnet-bc3xxxx","EmrManagedSlaveSecurityGroup":"sg-0da3dxxxx","EmrManagedMasterSecurityGroup":"sg-0fxxxx"}' --release-label emr-5.28.0 --log-uri 's3n://aws-logs-348134392524-us-west-2/elasticmapreduce/' --steps '' --auto-scaling-role EMR_AutoScaling_DefaultRole --ebs-root-volume-size 10 --service-role EMR_DefaultRole --enable-debugging --name 'benchmark-0106' --scale-down-behavior TERMINATE_AT_TASK_COMPLETION --region us-west-2
```

### EMR Step configuration

Jar Location: command-runner.jar

Main class: None

Action on failure: Continue

Arguments:

```
spark-submit --deploy-mode cluster --driver-cores 4 --driver-memory 8G --executor-cores 2 --executor-memory 8G --num-executors 10 --conf spark.dynamicAllocation.enabled=false --conf spark.executor.memoryOverhead=2G --conf spark.speculation=false --conf spark.speculation.multiplier=3 --conf spark.speculation.quantile=0.9 --conf spark.sql.broadcastTimeout=7200 --conf spark.sql.crossJoin.enabled=true --conf spark.sql.parquet.mergeSchema=false --conf spark.sql.parquet.filterPushdown=true --conf spark.hadoop.fs.s3a.connection.timeout=1200000 --conf spark.hadoop.fs.s3a.path.style.access=true --conf spark.hadoop.fs.s3a.connection.maximum=200 --conf spark.hadoop.fs.s3a.fast.upload=true --conf spark.hadoop.mapreduce.outputcommitter.factory.scheme.s3a=org.apache.hadoop.fs.s3a.commit.S3ACommitterFactory --conf spark.hadoop.fs.s3a.committer.name=directory --conf spark.hadoop.fs.s3a.committer.staging.conflict-mode=append --class com.amazonaws.eks.tpcds.BenchmarkSparkSQL s3a://spark-k8s-data/libs/eks-spark-examples-assembly-1.0.jar s3a://spark-k8s-data/TPCDS-TEST-1T s3a://spark-k8s-data/BENCHMARK-RESULT /opt/tpcds-kit/tools 1000 10 false q70-v2.4,q82-v2.4,q64-v2.4 true
```

> Note: EMR enables `spark.dynamicAllocation.enabled` by default. In order to control the size of executors, you need to explicitly set `spark.dynamicAllocation.enabled=false`.

> Note: EMR support S3 with EMRFS, if you like to use EMRFS, please remove S3A related configurations and change path from `s3a://` to `s3://`.