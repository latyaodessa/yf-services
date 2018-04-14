package yf.elastic.reindex.bulkworkflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import yf.core.PropertiesReslover;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;
import yf.user.UserConverter;
import yf.user.dto.UserDto;
import yf.user.dto.UserSocialTypeEnum;
import yf.user.entities.User;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

public class UserBulkWorkflow extends AbstractBulkReindexWorkflow<User, UserDto> {
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
			 UserDto dto = null;
			 
			if(entity.getType().equals(UserSocialTypeEnum.VK.name())){
//	             dto = userConverter.userVKtoUserElastic((VKUser) entity);
			}
			if(entity.getType().equals(UserSocialTypeEnum.FB.name())){
//	             dto = userConverter.toFbUserDto((FBUser) entity);
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
	public void addEntityToBulk(UserDto dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption)
			throws JsonProcessingException {
		if(bulkOption == BulkOptions.INDEX){
//	    	IndexRequestBuilder indexBuilder = prepareIndex(dto, elasticWorkflow.getDocumentId(dto.getId()), INDEX.iterator().next());
//			if (indexBuilder == null) { return; }
//			bulkRequest.add(indexBuilder);
		}
		
	}
	
}
