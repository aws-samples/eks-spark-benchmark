

### Spot
Use Mutating admission webhook to add node affinity. Need spot label

### driver failover
what about some name?  deletion and creation at the same time

### RAM based Empty Dir
https://issues.apache.org/jira/browse/SPARK-25262


1. Mount emptyDir with RAM backed volumes
2. Extend disk capacity of kubelet workspace
spark.kubernetes.local.dirs.tmpfs=true


### multiple disk [SPARK-27499]
https://github.com/apache/spark/pull/24879/files

kubelet work directory can only be mounted on one disk, so that the spark scratch space only use ONE disk

Use specified volumes mount as spark local directory SPARK-27499
spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.mount.path=/data/mnt-c
spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.options.path=/data/mnt-c

spark.kubernetes.executor.volumes.hostPath.spark-local-dir-2.mount.path=/data/mnt-d
spark.kubernetes.executor.volumes.hostPath.spark-local-dir-2.options.path=/data/mnt-d

spark.kubernetes.executor.volumes.hostPath.spark-local-dir-3.mount.path=/data/mnt-e
spark.kubernetes.executor.volumes.hostPath.spark-local-dir-3.options.path=/data/mnt-e

spark.local.dir=/data/mnt-c,/data/mnt-d,/data/mnt-e


Blog:

Performance optimization
- K8s - SPAKR_LOCAL_DIR
- S3 - hadoop 3.1 S3A committer.
- S3 - fast writter, s3 connection

Enterprise usage
 - resource allocation
 - spot
 - Isolation
 - Monitoring - Job Level. Cluster Level for Spark



./bin/spark-submit \
--master k8s://https://59CD6D370B1A60DBD55C9827BCE1D5F4.sk1.us-west-2.eks.amazonaws.com \
--deploy-mode cluster \
--name spark-pi \
--class org.apache.spark.examples.SparkPi  \
--conf spark.executor.instances=2 \
--conf spark.kubernetes.local.dirs.tmpfs=true \
--conf spark.kubernetes.container.image=seedjeffwan/spark:2.4.5-SNAPSHOT \
--conf spark.kubernetes.driver.pod.name=local-test-driver \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark local:///opt/spark/examples/target/original-spark-examples_2.11-2.4.5-SNAPSHOT.jar

git cherry-pick da6fa3828bb824b65f50122a8a0a0d4741551257
git cherry-pick 780d176136e911acb559c2384896be0a36f71d87




./bin/spark-submit \
--master k8s://https://59CD6D370B1A60DBD55C9827BCE1D5F4.sk1.us-west-2.eks.amazonaws.com \
--deploy-mode cluster \
--name zspark-pi \
--class org.apache.spark.examples.SparkPi  \
--conf spark.executor.instances=2 \
--conf spark.local.dir=/tmp/mnt-1,/tmp/mnt-2,/tmp/mnt-3 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.mount.path=/tmp/mnt-1 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.options.path=/tmp/mnt-1 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-2.mount.path=/tmp/mnt-2 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-2.options.path=/tmp/mnt-2 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-3.mount.path=/tmp/mnt-3 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-3.options.path=/tmp/mnt-3 \
--conf spark.kubernetes.container.image=seedjeffwan/spark:2.4.5-SNAPSHOT \
--conf spark.kubernetes.driver.pod.name=zlocal-test-driver \
--conf spark.kubernetes.authenticate.driver.serviceAccountName=spark local:///opt/spark/examples/target/original-spark-examples_2.11-2.4.5-SNAPSHOT.jar


Use specified volumes mount as spark local directory SPARK-27499

--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.mount.path=/tmp/mnt-1 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-1.options.path=/tmp/mnt-1 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-2.mount.path=/tmp/mnt-2 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-2.options.path=/tmp/mnt-2 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-3.mount.path=/tmp/mnt-3 \
--conf spark.kubernetes.executor.volumes.hostPath.spark-local-dir-3.options.path=/tmp/mnt-3 \


spark.local.dir=/tmp/mnt-1,/tmp/mnt-2,/tmp/mnt-3

Conclusion:
1. executor doesn't use memory
2. Multiple doesn't work..
3. revert seedjeffwan/2.4.5-SNAPSHOT


19/09/08 23:54:29 ERROR Utils: Uncaught exception in thread kubernetes-executor-snapshots-subscribers-1
io.fabric8.kubernetes.client.KubernetesClientException: Failure executing: POST at: https://kubernetes.default.svc/api/v1/namespaces/default/pods. Message: Pod "zspark-pi-1567986850584-exec-1" is invalid: [spec.volumes[3].name: Duplicate value: "spark-local-dir-1", spec.volumes[4].name: Duplicate value: "spark-local-dir-3", spec.volumes[5].name: Duplicate value: "spark-local-dir-2", spec.containers[0].volumeMounts[3].mountPath: Invalid value: "/tmp/mnt-1": must be unique, spec.containers[0].volumeMounts[4].mountPath: Invalid value: "/tmp/mnt-3": must be unique, spec.containers[0].volumeMounts[5].mountPath: Invalid value: "/tmp/mnt-2": must be unique]. Received status: Status(apiVersion=v1, code=422, details=StatusDetails(causes=[StatusCause(field=spec.volumes[3].name, message=Duplicate value: "spark-local-dir-1", reason=FieldValueDuplicate, additionalProperties={}), StatusCause(field=spec.volumes[4].name, message=Duplicate value: "spark-local-dir-3", reason=FieldValueDuplicate, additionalProperties={}), StatusCause(field=spec.volumes[5].name, message=Duplicate value: "spark-local-dir-2", reason=FieldValueDuplicate, additionalProperties={}), StatusCause(field=spec.containers[0].volumeMounts[3].mountPath, message=Invalid value: "/tmp/mnt-1": must be unique, reason=FieldValueInvalid, additionalProperties={}), StatusCause(field=spec.containers[0].volumeMounts[4].mountPath, message=Invalid value: "/tmp/mnt-3": must be unique, reason=FieldValueInvalid, additionalProperties={}), StatusCause(field=spec.containers[0].volumeMounts[5].mountPath, message=Invalid value: "/tmp/mnt-2": must be unique, reason=FieldValueInvalid, additionalProperties={})], group=null, kind=Pod, name=zspark-pi-1567986850584-exec-1, retryAfterSeconds=null, uid=null, additionalProperties={}), kind=Status, message=Pod "zspark-pi-1567986850584-exec-1" is invalid: [spec.volumes[3].name: Duplicate value: "spark-local-dir-1", spec.volumes[4].name: Duplicate value: "spark-local-dir-3", spec.volumes[5].name: Duplicate value: "spark-local-dir-2", spec.containers[0].volumeMounts[3].mountPath: Invalid value: "/tmp/mnt-1": must be unique, spec.containers[0].volumeMounts[4].mountPath: Invalid value: "/tmp/mnt-3": must be unique, spec.containers[0].volumeMounts[5].mountPath: Invalid value: "/tmp/mnt-2": must be unique], metadata=ListMeta(_continue=null, resourceVersion=null, selfLink=null, additionalProperties={}), reason=Invalid, status=Failure, additionalProperties={}).





19/09/08 23:54:29 ERROR Utils: Uncaught exception in thread kubernetes-executor-snapshots-subscribers-1
io.fabric8.kubernetes.client.KubernetesClientException: Failure executing: POST at: https://kubernetes.default.svc/api/v1/namespaces/default/pods. Message:
Pod "zspark-pi-1567986850584-exec-1" is invalid: [
spec.volumes[3].name: Duplicate value: "spark-local-dir-1",
spec.volumes[4].name: Duplicate value: "spark-local-dir-3",
spec.volumes[5].name: Duplicate value: "spark-local-dir-2",
spec.containers[0].volumeMounts[3].mountPath: Invalid value: "/tmp/mnt-1": must be unique,
spec.containers[0].volumeMounts[4].mountPath: Invalid value: "/tmp/mnt-3": must be unique,
spec.containers[0].volumeMounts[5].mountPath: Invalid value: "/tmp/mnt-2": must be unique].

Received status: Status(apiVersion=v1, code=422, details=StatusDetails(causes=[StatusCause(field=
spec.volumes[3].name, message=Duplicate value: "spark-local-dir-1", reason=FieldValueDuplicate, additionalProperties={}), StatusCause(field=spec.volumes[4].name, message=Duplicate value: "spark-local-dir-3", reason=FieldValueDuplicate, additionalProperties={}), StatusCause(field=spec.volumes[5].name, message=Duplicate value: "spark-local-dir-2", reason=FieldValueDuplicate, additionalProperties={}), StatusCause(field=spec.containers[0].volumeMounts[3].mountPath, message=Invalid value: "/tmp/mnt-1": must be unique, reason=FieldValueInvalid, additionalProperties={}), StatusCause(field=spec.containers[0].volumeMounts[4].mountPath, message=Invalid value: "/tmp/mnt-3": must be unique, reason=FieldValueInvalid, additionalProperties={}), StatusCause(field=spec.containers[0].volumeMounts[5].mountPath, message=Invalid value: "/tmp/mnt-2": must be unique, reason=FieldValueInvalid, additionalProperties={})], group=null, kind=Pod, name=zspark-pi-1567986850584-exec-1, retryAfterSeconds=null, uid=null, additionalProperties={}), kind=Status, message=Pod "zspark-pi-1567986850584-exec-1" is invalid: [spec.volumes[3].name: Duplicate value: "spark-local-dir-1", spec.volumes[4].name: Duplicate value: "spark-local-dir-3", spec.volumes[5].name: Duplicate value: "spark-local-dir-2", spec.containers[0].volumeMounts[3].mountPath: Invalid value: "/tmp/mnt-1": must be unique, spec.containers[0].volumeMounts[4].mountPath: Invalid value: "/tmp/mnt-3": must be unique, spec.containers[0].volumeMounts[5].mountPath: Invalid value: "/tmp/mnt-2": must be unique], metadata=ListMeta(_continue=null, resourceVersion=null, selfLink=null, additionalProperties={}), reason=Invalid, status=Failure, additionalProperties={}).