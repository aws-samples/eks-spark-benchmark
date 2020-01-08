### Mount NVMe to instances

EBS volumes are exposed as NVMe block devices on Nitro-based instances. Some memory optimized instance types like r5d has local NVMe SSD but this is not mounted to instance by default. In order to use it, you would need to mount disk to instances and use them for Spark scratch space.

```
nodeGroups:
  - name: spark-nodes
    instanceType: r5d.xlarge
    availabilityZones: ["us-west-2b"]
    desiredCapacity: 1
    minSize: 0
    maxSize: 4
    volumeSize: 50
    ssh:
      allow: true
      publicKeyPath: '~/.ssh/id_rsa.pub'
    preBootstrapCommands:
      - IDX=1
      - for DEV in /dev/disk/by-id/nvme-Amazon_EC2_NVMe_Instance_Storage_*-ns-1; do  mkfs.xfs ${DEV};mkdir -p /local${IDX};echo ${DEV} /local${IDX} xfs defaults,noatime 1 2 >> /etc/fstab; IDX=$((${IDX} + 1)); done
      - mount -a
```

```
[ec2-user@ip-192-168-3-48 ~]$ df
Filesystem     1K-blocks    Used Available Use% Mounted on
devtmpfs         7961796       0   7961796   0% /dev
tmpfs            7975256       0   7975256   0% /dev/shm
tmpfs            7975256     688   7974568   1% /run
tmpfs            7975256       0   7975256   0% /sys/fs/cgroup
/dev/nvme0n1p1  52416492 2300460  50116032   5% /
/dev/nvme1n1   146412848  178932 146233916   1% /local
tmpfs            1595052       0   1595052   0% /run/user/1000

[ec2-user@ip-192-168-3-48 ~]$ lsblk
'NAME          MAJ:MIN RM   SIZE RO TYPE MOUNTPOINT
nvme0n1       259:0    0    50G  0 disk
├─nvme0n1p1   259:2    0    50G  0 part /
└─nvme0n1p128 259:3    0     1M  0 part
nvme1n1       259:1    0 139.7G  0 disk /local
```

### Single AZ node Groups

Users like to run kubernetes node groups in multiple availability zones (AZs) in order to mitigate the risk of not having enough capacity or failures in a single AZ. There's two potential problems. One is Cross-AZ latency is not good as Inter-AZ latency within a region which is designed to have low latency. Spark shuffle is an expensive operation involving disk I/O, data serialization and definitely network I/O. Lower latency can give better performance on shuffle. The other issue is Cross-AZ will increased data transfer costs between zones.

Based on your tolenrance of lacking resources or failure, You can choose single-AZ cluster or use advanced scheduling like zone-awareness or task topology in a multi-AZ cluster.

### FSx for Lustre

If user doesn't like to use S3 directly, we recommend to use [Amazon FSx for Lustre](https://aws.amazon.com/fsx/lustre/)

Amazon FSx for Lustre provides a high-performance file system optimized for fast processing of workloads such as machine learning, high performance computing (HPC), video processing, financial modeling, and electronic design automation (EDA). These workloads commonly require data to be presented via a fast and scalable file system interface, and typically have data sets stored on long-term data stores like Amazon S3.

Please check [Amazon FSx for Lustre CSI Driver](https://github.com/kubernetes-sigs/aws-fsx-csi-driver) to use FSx for Lustre on EKS.

> Note: FSx for Lustre doesn't support multi-AZ at this moment.