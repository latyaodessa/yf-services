package vk.logic.elastic;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

@Singleton
@Startup
public class ElasticSingleton {

    private JestClient client;

    @PostConstruct
    private void init() {
    
    	
    	JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig.Builder("http://localhost:9200")
                .multiThreaded(true)
                .build());
         client = factory.getObject();
	

    }
    public JestClient getClient() {
        return client;
    }

    @PreDestroy
    private void close() {
    	client.shutdownClient();
    }
}