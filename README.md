# roller
This is a basic implementation of a rolling file appender.Goal is to extract the JSON body from incoming web requests, and serialize the request body with Google protobuf into disk. This serialized file should roll after a configured time out.

# Build
`mvn clean package -DskipTests=true`

# Installation
## Pre-requisites
The target VM should have docker installed.

## Steps
Copy the installation directory into the target VM.
```
dos2unix installation/setup/target/scripts/install_roller_main.sh installation/setup/target/scripts/install_roller_main.sh
chmod +x installation/setup/target/scripts/install_roller_main.sh
installation/setup/target/scripts/install_roller_main.sh
```
