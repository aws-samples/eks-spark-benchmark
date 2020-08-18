FROM python:3.7.3
USER root

ARG NB_USER=jovyan
ENV NB_USER $NB_USER

ENV NB_UID 1000
ENV HOME /home/$NB_USER
ENV NB_PREFIX /
ENV PATH $HOME/.local/bin:$PATH


# RUN /usr/bin/python3 -m pip3 install --upgrade pip3
RUN pip3 install awscli pyspark findspark
# Install base python3 packages
RUN pip3 --no-cache-dir install jupyter-console==6.0.0 jupyterlab

RUN wget http://archive.apache.org/dist/spark/spark-3.0.0/spark-3.0.0-bin-hadoop3.2.tgz
RUN tar xvzf spark-3.0.0-bin-hadoop3.2.tgz
RUN mv spark-3.0.0-bin-hadoop3.2 /opt/spark-3.0.0
RUN ln -s /opt/spark-3.0.0 /opt/spark
RUN rm spark-3.0.0-bin-hadoop3.2.tgz
ENV SPARK_HOME=/opt/spark
ENV PATH=$SPARK_HOME/bin:$PATH
RUN apt-get update -y
RUN apt-get install -y openjdk-8-jre gcc \
     python3 python-dev python3-dev \
     build-essential libssl-dev libffi-dev \
     libxml2-dev libxslt1-dev zlib1g-dev \
     python-pip


EXPOSE 8888
CMD ["sh","-c", "jupyter notebook --notebook-dir=/home/${NB_USER} --ip=0.0.0.0 --no-browser --allow-root --port=8888 --NotebookApp.token='' --NotebookApp.password='' --NotebookApp.allow_origin='*' --NotebookApp.base_url=${NB_PREFIX}"]

