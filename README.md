# Apachepulsar-example

Install Docker desktop for Windows

Open Windows Powershell and type in
docker run -it -p 6650:6650 -p 8080:8080 --mount source=pulsardata1,target=/pulsar/data --mount source=pulsarconf1,target=/pulsar/conf apachepulsar/pulsar:2.6.0 bin/pulsar standalone

This command generates a standlone cluster in your docker

Open a new Windows powershell  and Execute the below command to make any changes to files and folders inside the docker.

docker exec -it  <container-name> /bin/bash
Example:- docker exec -it  eager_bell /bin/bash 

// To generate the jwt token based on base64 encoded secretkey using Pulsar Cli
bin/pulsar tokens create-secret-key --output  /opt/my-secret.key --base64

// To generate the jwt token based on public and privatekey in Pulsar cli
bin/pulsar tokens create-key-pair --output-private-key my-private.key --output-public-key my-public.key

// To generate the jwt token for a specific subject and assign an expiry and store the secret key in the path /opt/my-secret.key
bin/pulsar tokens create --private-key file:///opt/my-private.key --subject test-user --expiry-time 1y

// copy the secretkey generated in pulsar cli on the console to use it your code
cat my-secret.key

In case eiditor is not installed in Docker use the below commands
apt update
apt install vim

type in cd /pulsar/conf   to navigate to configuartion folder
and update
broker.conf,standalone.conf,client.conf 

With the following parameters
authenticationEnabled=true
authorizationEnabled=true
authenticationProviders=org.apache.pulsar.broker.authentication.AuthenticationProviderToken
# If using secret key
tokenSecretKey=file:///path/to/secret.key

For more documentation and details refer

https://pulsar.apache.org/docs/en/standalone-docker/
