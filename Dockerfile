FROM tomcat

ADD ./deployment/XACML-WebPAP2_blockchain.war /usr/local/tomcat/webapps/
ADD ./deployment/XACMLServletPDP.war /usr/local/tomcat/webapps/
ADD ./PAPConfigData /usr/local/tomcat/PAPConfigData

EXPOSE 8080:8080
#EXPOSE 8443:8443
