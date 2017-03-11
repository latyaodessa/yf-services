package yf.elastic.reindex.bulkworkflow;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;

import com.fasterxml.jackson.core.JsonProcessingException;

import yf.core.PropertiesReslover;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;
import yf.user.UserConverter;
import yf.user.UserTypes;
import yf.user.dto.UserElasticDTO;
import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.VKUser;

public class UserBulkWorkflow extends AbstractBulkReindexWorkflow<User, UserElasticDTO> {
	@Inject
	private PropertiesReslover properties;
	@Inject
	private ElasticWorkflow elasticWorkflow;
	@Inject
	private NativeElasticSingleton nativeElasticClient;
	@Inject
	private UserConverter userConverter;
	
	@PostConstruct
	protected void initIndecies() {
		INDEX.add(properties.get("elastic.index.user"));
		TYPE = properties.get("elastic.index.user.type");
	}

	@Override
	public void deleteIndicies() {
		elasticWorkflow.deleteIndicies(INDEX);		
	}

	@Override
	public void execute(List<User> entities) {
		BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
		bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE); // ??
				
		for(User entity : entities){
			 UserElasticDTO dto = null;
			 
			if(entity.getYf_user_type().equals(UserTypes.VK.name())){
	             dto = userConverter.userVKtoUserElastic((VKUser) entity);
			}
			if(entity.getYf_user_type().equals(UserTypes.FB.name())){
	             dto = userConverter.userFBtoUserElastic((FBUser) entity);
			}
		
			try {
				addEntityToBulk(dto, bulkRequest, BulkOptions.INDEX);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			LOGGER.log(java.util.logging.Level.WARNING, "BULK REINDEX FAILUER");
			}		
	}

	@Override
	public void addEntityToBulk(UserElasticDTO dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption)
			throws JsonProcessingException {
		if(bulkOption == BulkOptions.INDEX){
	    	IndexRequestBuilder indexBuilder = prepareIndex(dto, elasticWorkflow.getDocumentId(dto.getId()), INDEX.iterator().next());
			if (indexBuilder == null) { return; }
			bulkRequest.add(indexBuilder);
		}
		
	}
	
}
