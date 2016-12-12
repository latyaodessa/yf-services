package yf.elastic.reindex.bulkworkflow;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import org.elasticsearch.action.update.UpdateRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

import yf.core.PropertiesReslover;
import yf.dashboard.postphoto.PostPhotoDashboardConverter;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;

public class PostDashboardBulkWorkflow extends AbstractBulkReindexWorkflow<UserSavedPosts, PostDashboardElasticDTO> {
	@Inject
	private PropertiesReslover properties;
	@Inject
	private ElasticWorkflow elasticWorkflow;
	@Inject
	private NativeElasticSingleton nativeElasticClient;
	@Inject
	private PostPhotoDashboardConverter postPhotoDashboardConverter;
	
	@Override
	@PostConstruct
	protected void initIndecies() {
		INDEX.add(properties.get("elastic.index.dashboard.saved.post"));
		TYPE = properties.get("elastic.type.dashboard");
	}

	@Override
	public void deleteIndicies() {
		elasticWorkflow.deleteIndicies(INDEX);				
	}

	@Override
	public void execute(List<UserSavedPosts> entities) {
		
		BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
		bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
				
		for(UserSavedPosts entity : entities){
			PostDashboardElasticDTO dto = postPhotoDashboardConverter.toPostDashboardElasticDTO(entity);
			try {
				addEntityToBulk(dto, bulkRequest, BulkOptions.INDEX);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		};
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			LOGGER.log(java.util.logging.Level.WARNING, "BULK REINDEX FAILUER");
			}				
	}

	@Override
	public void addEntityToBulk(PostDashboardElasticDTO dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption)
			throws JsonProcessingException {
		
	if(bulkOption == BulkOptions.INDEX){
    	IndexRequestBuilder indexBuilder = prepareIndex(dto, elasticWorkflow.getDocumentId(dto.getId()), INDEX.iterator().next());
		if (indexBuilder == null) { return; }
		bulkRequest.add(indexBuilder);
	}
		
	}

	@Override
	protected IndexRequestBuilder prepareIndex(PostDashboardElasticDTO dto, String id, String index) {
		if (dto == null) { return null; }
		try {
			return index!= null ? nativeElasticClient.getClient()
									.prepareIndex(index, TYPE, id)
									.setSource(new ObjectMapper().writeValueAsString(dto))
									:null;
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected UpdateRequestBuilder prepareUpdateIndex(PostDashboardElasticDTO dto, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DeleteRequestBuilder prepareDeleteIndex(PostDashboardElasticDTO dto, String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIndex(UserSavedPosts entity, Map<String, String> tag_index) {
		// TODO Auto-generated method stub
		return null;
	}

}
