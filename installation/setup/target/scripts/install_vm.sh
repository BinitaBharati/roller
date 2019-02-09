#!/bin/bash

editConfigFiles() {
sudo cp $1 $1.orig
sudo sed -i "/${2}/c\\${3}" $1
}

#Ref for ubuntu/xenial: https://medium.com/@Grigorkh/how-to-install-docker-on-ubuntu-16-04-3f509070d29c
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
sudo apt-get update
apt-cache policy docker-ce
sudo apt-get install -y docker-ce

#Install docker-compose
#Ref: https://www.digitalocean.com/community/tutorials/how-to-install-docker-compose-on-ubuntu-16-04
sudo curl -L https://github.com/docker/compose/releases/download/1.23.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose


#In Ubuntu xenial vbox, ssh password authentication is switched off by default.Enable it.Once the work is done,
#you could disable password based authentication and let only key based authentication remain.
editConfigFiles /etc/ssh/sshd_config 'PasswordAuthentication no' 'PasswordAuthentication yes'

#Restart ssh servies for the above ssh config change to take effect.
sudo service ssh restart

#install python3 on Xenial. Django will be installed as part of docker image.
#sudo apt-get update;sudo apt-get -y install dos2unix
#dos2unix /vagrant/target/scripts/install_python3.sh  /vagrant/target/scripts/install_python3.sh
#chmod +x /vagrant/target/scripts/install_python3.sh
#/vagrant/target/scripts/install_python3.sh
#Add entry in /etc/hosts
cp /etc/hosts /home/vagrant/hosts.orig
echo "192.168.10.12 net1mc1" >> /home/vagrant/hosts.orig
sudo cp /home/vagrant/hosts.orig /etc/hosts

#install dos2unix
sudo apt-get -y install dos2unix
