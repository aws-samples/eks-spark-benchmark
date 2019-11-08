
### Spot
Use Mutating admission webhook to add node affinity. Need spot label

### driver failover
what about some name?  deletion and creation at the same time

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