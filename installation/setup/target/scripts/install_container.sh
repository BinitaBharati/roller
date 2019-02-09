#!/bin/bash
set -x

echo "install-container.sh : your env are hostIp=$hostIp"

apt install -y net-tools

dos2unix scripts/install_java.sh scripts/install_java.sh
chmod +x scripts/install_java.sh
scripts/install_java.sh

java -cp target/roller-0.0.1-SNAPSHOT.jar  bharati.binita.roller.Main &


while true
do
	echo "Press [CTRL+C] to stop.."
	sleep 1
done




