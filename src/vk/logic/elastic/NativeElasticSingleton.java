//package vk.logic.elastic;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//
//@Singleton
//@Startup
//public class NativeElasticSingleton {
//	
//	private TransportClient client;
//	
//	
//    @PostConstruct
//    private void init() {
//    	
//    	try {
//    		
//			client = new PreBuiltTransportClient(Settings.EMPTY);
////			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host1"), 9300))
////			        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("host2"), 9300));
//		} catch (Exception e) {
//
//		}
//
//    }
//    
//    public TransportClient getClient(){
//    	return client;
//    }
//    
//    @PreDestroy
//    private void close() {
//    	client.close();
//    }
//
//}
