#This is a sample Image 
#docker unbuntu image 
FROM ubuntu:16.04 
MAINTAINER binita.bharati@gmail.com 

RUN mkdir -p /home/vagrant/dockerws/scripts/
RUN mkdir -p /home/vagrant/dockerws/target/logs/
RUN mkdir -p /home/vagrant/dockerws/target/output/
WORKDIR /home/vagrant/dockerws/

#Copy all the required files from host's file system to the container file system.
COPY setup/target/scripts/install_java.sh scripts/
COPY setup/target/scripts/install_container.sh scripts/
COPY target/ target/

RUN apt-get update;apt-get -y install dos2unix

RUN dos2unix scripts/install_container.sh scripts/install_container.sh;chmod +x scripts/install_container.sh

ENTRYPOINT ["/home/vagrant/dockerws/scripts/install_container.sh"]
