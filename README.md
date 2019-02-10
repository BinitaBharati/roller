# roller
This is a basic implementation of a rolling file appender. The goal is to extract the JSON body from incoming web requests, and serialize the request body with Google protobuf into disk. This serialized file should roll after a configured time out.

# Build
`mvn clean package -DskipTests=true`

# Installation
## Pre-requisites
The target VM should have Docker installed.

## Steps
Copy the `installation` directory into the target VM.
```
dos2unix installation/setup/target/scripts/install_roller_main.sh installation/setup/target/scripts/install_roller_main.sh
chmod +x installation/setup/target/scripts/install_roller_main.sh
installation/setup/target/scripts/install_roller_main.sh
```

# Tests
This project includes a basic test case. The goal of the test case is to start a local roller web server on the run, send it X amount of POST requests at  `/hichki`, and then send it a single GET request at `/hichki`. The goal of the GET request is to count the number of times the POST request has been invoked by deserializing the disk file(s).

To run the test case, invoke the Junit TestCase at `bharati.binita.roller.TestRollingService`
You might have to edit the property : `file.rollover.path` according to your local machine path.
