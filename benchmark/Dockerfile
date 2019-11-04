#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Use the one from spark kubernetes example
FROM amazonlinux:2 as builder

RUN mkdir -p /tmp

ENV TPCDS_KIT_VERSION "master"

RUN yum group install -y "Development Tools" && \
    git clone https://github.com/databricks/tpcds-kit.git -b ${TPCDS_KIT_VERSION} /tmp/tpcds-kit && \
    cd /tmp/tpcds-kit/tools && \
    make OS=LINUX

FROM seedjeffwan/spark:2.4.5-SNAPSHOT
LABEL maintainer="Jiaxin Shan"

COPY --from=builder /tmp/tpcds-kit/tools /opt/tpcds-kit/tools
COPY ./target/scala-2.11/*jar ${SPARK_HOME}/examples/jars/
