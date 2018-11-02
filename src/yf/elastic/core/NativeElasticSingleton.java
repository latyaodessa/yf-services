package yf.elastic.core;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import yf.core.JNDIPropertyHelper;

@Singleton
@Startup
public class NativeElasticSingleton {

    private static final Logger LOG = Logger.getLogger(NativeElasticSingleton.class.getName());
    private static final String ELASTIC_HOST_JNDI = "yf.elastic.host";
    private static final String ELASTIC_PORT_JNDI = "yf.elastic.port";
    private TransportClient client;

    @SuppressWarnings("resource")
    @PostConstruct
    protected void init() {

        String host = new JNDIPropertyHelper().lookup(ELASTIC_HOST_JNDI);
        int port = new JNDIPropertyHelper().lookup(ELASTIC_PORT_JNDI);

        try {
            client = new PreBuiltTransportClient(Settings.EMPTY).addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),
                    port));
        } catch (Exception e) {
            LOG.log(Level.SEVERE,
                    " ELASTICSEARCH EXCEPTION " + e);
        }
    }

    public TransportClient getClient() {
        return client;
    }

    @PreDestroy
    protected void close() {
        client.close();
    }
}
