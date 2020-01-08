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