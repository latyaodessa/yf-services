FROM payara/server-full:4.181

COPY docker/postgresql-42.2.2.jar glassfish/domains/domain1/lib/postgresql-42.2.2.jar
COPY docker/config/domain_custom.xml glassfish/domains/domain1/config/domain.xml
COPY docker/config/admin-keyfile glassfish/domains/domain1/config/admin-keyfile

COPY docker/yf-services.war glassfish/domains/domain1/autodeploy/yf-services.war

EXPOSE 4848
EXPOSE 8080
EXPOSE 8181
