package yf.elastic.core;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.IndicesAdminClient;

import yf.core.PropertiesReslover;

public class ElasticWorkflow {
	
    private static final Logger LOG = Logger.getLogger(ElasticWorkflow.class.getName());

	@Inject
	PropertiesReslover properties;
	@Inject
	NativeElasticSingleton nativeElasticClient;

    
    public String getDocumentId(final Long id) {
        return String.valueOf(id);
    }
    
    public <T> String objectToSource(T obj){
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    
	
	public void deleteIndicies(Map <String, String> tag_indecies){	
		tag_indecies.entrySet().forEach( i -> {
				deleteIndex(i.getValue());
		});
	}
	public void deleteIndicies(Set <String> indecies){
		indecies.forEach( i -> {
				deleteIndex(i);
		});
	}

	
	public void deleteIndex(String index){
		
		IndicesAdminClient adminClinet = nativeElasticClient.getClient()
				.admin().indices();
		
		if(isIndexExist(adminClinet, index)){
			DeleteIndexResponse delete = nativeElasticClient.getClient()
					.admin()
					.indices()
					.delete(new DeleteIndexRequest(index))
					.actionGet();

				if (!delete.isAcknowledged()) {
				LOG.log(Level.SEVERE,"Index wasn't deleted");
					}
		}
	}
	
	public boolean isIndexExist(final IndicesAdminClient adminClinet,final String index){
		return adminClinet.prepareExists(index).execute().actionGet().isExists();
	}

}
