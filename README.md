# License

PAP-PDP Project source code files are made avaialable under the Apache License, Version 2.0 (Apache-2.0), located into the LICENSE file.

# Introduction

This project corresponds to the implementation of the XACML framework. It comprises a Policy Administration Point (PAP) which is responsible for managing the authorisation policies, and a Policy Decision Point (PDP).


# Configuration config.cfg file

Before launching the project, it's necessary to review the blockchain.conf file:

```sh
cd projectPath / xacml_pap_pdp / PAPConfigData
vi blockchain.conf
```

This file must have defined the blockchain endpoint, the domain and resources. 

# Configuration docker-compose.yml file

There isn't any additional required configuration in this file.

# Prerequisites

To run this project is neccessary to install the docker-compose tool.

https://docs.docker.com/compose/install/

Launch then next components:

- Blockchain component running.

# Installation / Execution.

After the review of blockchain.conf file and docker-compose finle, we are going to obtain then Docker image. To do this, you have to build a local one, thus:

```sh
cd projectPath / xacml_pap_pdp
./build.sh
```

The build.sh file contains docker build -t iotcrawler/pap-pdp ./ command.

Finally, to launch the connector image, we use the next command:

```sh
cd projectPath / xacml_pap_pdp
docker-compose up -d
```

# Monitoring.

- To test if the container is running:

```sh
docker ps -as
```

The system must return that the status of the XACML-PAP-PDP container is up.

- To show container logs.

```sh
docker-compose logs authcomponents
```

# XACML functionality.

- Policy Administration Point (PAP) is accessible through WEB explorer at http://localhost:8080/XACML-WebPAP2_blockchain.

- Policy Decision Point (PDP) is waiting for POST request at http://localhost:8080/XACMLServletPDP/. An example of a request could be:

```sh
curl --location --request POST 'http://localhost:8080/XACMLServletPDP/' \
--header 'Content-Type: text/plain' \
--data-raw '<Request xmlns="urn:oasis:names:tc:xacml:2.0:context:schema:os">
   <Subject SubjectCategory="urn:oasis:names:tc:xacml:1.0:subject-category:access-subject">
       <Attribute AttributeId="urn:oasis:names:tc:xacml:2.0:subject:role" DataType="http://www.w3.org/2001/XMLSchema#string">
           <AttributeValue>{{subject}}</AttributeValue>
       </Attribute>  
   </Subject>
   
   <Resource>
       <Attribute AttributeId="urn:oasis:names:tc:xacml:1.0:resource:resource-id" DataType="http://www.w3.org/2001/XMLSchema#string">
           <AttributeValue>{{resource}}</AttributeValue>
       </Attribute>
   </Resource> 

   <Action>
       <Attribute AttributeId="urn:oasis:names:tc:xacml:1.0:action:action-id" DataType="http://www.w3.org/2001/XMLSchema#string">
           <AttributeValue>{{action}}</AttributeValue>
       </Attribute>  
   </Action>

   <Environment/>
</Request>'
```

Where:
- subject: subject of the resource’s request. In DCapBAC scenario, it could correspond with a username (IDM). For example: “user1”
- resource:  endpoint + path of the resource’s request (protocol+IP+PORT+path). For example: “https://153.55.55.120:2354/ngsi-ld/v1/entities/urn:ngsi-ld:Sensor:humidity.201”.  In DCapBAC scenario, endpoint corresponds with the PEP-Proxy one.
- action: method of the resource’s request (“POST”, “GET”,  “PATCH”...)