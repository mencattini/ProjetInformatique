FROM jboss/wildfly:latest

ADD ./mysql-connector-java-5.1.32.jar ./config_wildfly.sh /opt/jboss/wildfly/customization/
ADD ./run_wildfly.sh /opt/

RUN ["/opt/jboss/wildfly/customization/config_wildfly.sh"]
RUN rm -rf /opt/jboss/wildfly/standalone/configuration/standalone_xml_history

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "--debug", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]

expose 8787
expose 9990
expose 8080
