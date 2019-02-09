#!/bin/bash
set -x



getip()
{
/sbin/ifconfig ${1:-eth0} | awk '/inet addr/ {print $2}' | awk -F: '{print $2}';
}

sudo apt-get update

cd /vagrant
sudo docker build --no-cache -t roller:latest -f /vagrant/setup/docker/Dockerfile .

#Get current host ip, based on that IP we need to invoke docker run with varying input args.
#With ubuntu xenial, host ip interface is named as enp0s8.
myip=$(getip enp0s8)
echo "install_roller_main.sh myip is $myip"
sudo docker run --network=host -td -e hostIp=$myip roller:latest