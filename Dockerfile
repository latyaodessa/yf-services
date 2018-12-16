FROM payara/server-full:4.181

COPY docker/postgresql-42.2.2.jar glassfish/domains/domain1/lib/postgresql-42.2.2.jar
COPY docker/config/domain_dev.xml glassfish/domains/domain1/config/domain.xml
COPY docker/config/admin-keyfile glassfish/domains/domain1/config/admin-keyfile

COPY target/yf-services.war glassfish/domains/domain1/autodeploy/yf-services.war

EXPOSE 4848
EXPOSE 8080
EXPOSE 8181
EXPOSE 9009
EXPOSE 29009

ENTRYPOINT ["/opt/payara41/bin/startInForeground.sh", "--passwordfile=/opt/pwdfile", "-d", "--postbootcommandfile=/opt/payara41/glassfish/domains/domain1"]
