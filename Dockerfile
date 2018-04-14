FROM payara/server-full:4.181

COPY setup/server/postgresql-42.2.2.jar glassfish/domains/domain1/lib

COPY setup/server/config/domain.xml glassfish/domains/domain1/config

COPY target/yf-services.war glassfish/domains/domain1/autodeploy