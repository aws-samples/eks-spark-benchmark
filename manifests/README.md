### Build and install big data stack

```
kustomize build spark-operator/base | kubectl apply -f -
```

### Create Spark service account

```
kubectl create serviceaccount spark
kubectl create clusterrolebinding spark-role --clusterrole=edit --serviceaccount=default:spark --namespace=default
```

You can use service account `spark` in your driver pod.


### Access Dashboard

#### Expose spark history server

```
kubectl port-forward svc/spark-history-server 18080:18080
```

#### Expose spark history server
```
kubectl port-forward svc/zeppelin-server 8000:80
```



Spark 3.0.0-SNAPSHOT with Hadoop 3.2
# S3 Committer
"spark.hadoop.mapreduce.outputcommitter.factory.scheme.s3a": "org.apache.hadoop.fs.s3a.commit.S3ACommitterFactory"
"spark.sql.sources.commitProtocolClass": "org.apache.spark.internal.io.cloud.PathOutputCommitProtocol"
"spark.sql.parquet.output.committer.class": "org.apache.hadoop.mapreduce.lib.output.BindingPathOutputCommitter"
# Magic Committer
"spark.hadoop.fs.s3a.committer.name": "magic"
"spark.hadoop.fs.s3a.committer.magic.enabled": "true"
"spark.hadoop.fs.s3a.metadatastore.impl": "org.apache.hadoop.fs.s3a.s3guard.DynamoDBMetadataStore"
"spark.hadoop.fs.s3a.s3guard.ddb.region": "us-west-2"
"spark.hadoop.fs.s3a.s3guard.ddb.table.create": "true"
# Staging Committer
"spark.hadoop.fs.s3a.committer.name": "directory"
"spark.hadoop.fs.s3a.committer.staging.conflict-mode": "append"


Spark 2.4.5-SNAPSHOT with Hadoop 3.1

# S3 Committer
"spark.hadoop.mapreduce.outputcommitter.factory.scheme.s3a": "org.apache.hadoop.fs.s3a.commit.S3ACommitterFactory"
# Staging Committer
"spark.hadoop.fs.s3a.committer.name": "directory"
"spark.hadoop.fs.s3a.committer.staging.conflict-mode": "append"


