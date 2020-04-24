#
#Copyright Odin Solutions S.L. All Rights Reserved.
#
#SPDX-License-Identifier: Apache-2.0
#

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
echo $DIR

docker rm xacml_dockerized 
docker run --privileged --name=xacml_dockerized \
	-v $DIR/deployment/XACML-WebPAP2_blockchain.war:/usr/local/tomcat/webapps/XACML-WebPAP2_blockchain.war \
	-v $DIR/deployment/XACMLServletPDP.war:/usr/local/tomcat/webapps/XACMLServletPDP.war \
	-v $DIR/PAPConfigData:/usr/local/tomcat/PAPConfigData \
	-it -p 8080:8080 tomcat


# Accessing the PAP Service 
# localhost:8080/XACML-WebPAP2_blockchain

# Accesing the PDP Service
# http://localhost:8080/XACMLServletPDP/
