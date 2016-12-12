package yf.elastic.reindex.bulkworkflow;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

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
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;
import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.post.parser.workflow.ParserPostConverter;

public class PostBulkWorkflow extends AbstractBulkReindexWorkflow<Post, PostElasticDTO>{
	@Inject
	private ParserPostConverter postConverter;
	@Inject
	private  PropertiesReslover properties;
	@Inject
	ElasticWorkflow elasticWorkflow;
	@Inject
	NativeElasticSingleton nativeElasticClient;
	

	@PostConstruct
	protected void initIndecies(){
		TAG_INDEX.put(properties.get("tag.vk.native"), properties.get("elastic.index.native"));
		TAG_INDEX.put(properties.get("tag.vk.sets"), properties.get("elastic.index.sets"));
		TAG_INDEX.put(properties.get("tag.vk.soft"), properties.get("elastic.index.soft"));
		TAG_INDEX.put(properties.get("tag.vk.black"), properties.get("elastic.index.black"));
		TAG_INDEX.put(properties.get("tag.vk.silhouettes"), properties.get("elastic.index.silhouettes"));
		TAG_INDEX.put(properties.get("tag.vk.80s90s"), properties.get("elastic.index.80s90s"));
		TAG_INDEX.put(properties.get("tag.vk.legs"), properties.get("elastic.index.legs"));
		TAG_INDEX.put(properties.get("tag.vk.art"), properties.get("elastic.index.art"));
		
		TYPE = properties.get("elastic.type.photo");

	}
	
	
	public void deleteIndicies(){
		elasticWorkflow.deleteIndicies(TAG_INDEX);
	}
	
	public void execute(List<Post> posts){
		
		
		BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
		bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE); // ??
				
		for(Post p : posts){
            PostElasticDTO dto = postConverter.toElasticPostDto(p);
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
	
    public void addEntityToBulkWithCustomIndex(final PostElasticDTO dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption, String index) throws JsonProcessingException {
        
		if(bulkOption == BulkOptions.INDEX){
	    	IndexRequestBuilder indexBuilder = prepareIndex(dto, elasticWorkflow.getDocumentId(dto.getId()), index);
			if (indexBuilder == null) { return; }
			bulkRequest.add(indexBuilder);
		}
    }
	
    public void addEntityToBulk(final PostElasticDTO dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption) throws JsonProcessingException {
       
		if(bulkOption == BulkOptions.INDEX){
	    	IndexRequestBuilder indexBuilder = prepareIndex(dto, elasticWorkflow.getDocumentId(dto.getId()), null);
			if (indexBuilder == null) { return; }
			bulkRequest.add(indexBuilder);
		}

		// throw exception if doc missing
		if(bulkOption == BulkOptions.UPDATE){
			UpdateRequestBuilder indexBuilder = prepareUpdateIndex(dto, elasticWorkflow.getDocumentId(dto.getId()));
			if (indexBuilder == null) { return; }
			bulkRequest.add(indexBuilder);
		}
		
		if(bulkOption == BulkOptions.DELETE){
			DeleteRequestBuilder indexBuilder = prepareDeleteIndex(dto, elasticWorkflow.getDocumentId(dto.getId()));
			if (indexBuilder == null) { return; }
			bulkRequest.add(indexBuilder);
		}
		

}
    
    protected IndexRequestBuilder prepareIndex(final PostElasticDTO dto, final String id, String index) {
					if (dto == null) { return null; }
					if(index == null) { index = getIndex(dto, TAG_INDEX);  };
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
    
    protected UpdateRequestBuilder prepareUpdateIndex(final PostElasticDTO dto, final String id) {
		if (dto == null) { return null; }

		try {
			String index = getIndex(dto, TAG_INDEX);
			return index!= null ? nativeElasticClient.getClient()
									.prepareUpdate(index, TYPE, id)
									.setDoc(new ObjectMapper().writeValueAsString(dto))
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
    
    protected DeleteRequestBuilder prepareDeleteIndex(final PostElasticDTO dto, final String id) {
		if (dto == null) { return null; }

			String index = getIndex(dto, TAG_INDEX);
			return index!= null ? nativeElasticClient.getClient()
									.prepareDelete(index, TYPE, id)
									:null;
}
    
	public String getIndex(Post dto, Map <String, String> tag_index){
		
		Entry<String, String> filtered = tag_index.entrySet().stream()
				.filter(map -> dto.getText().contains(map.getKey()))
				.findAny().orElse(null);
		
		String index = Optional.ofNullable(filtered).map(Entry<String, String>::getValue).orElse(null);	
		
		if(index == null){
			LOGGER.log(java.util.logging.Level.WARNING, "NO INDEX WERE FIND FOR" + dto.getText() 
			+ " ID " + dto.getId() );
		}
		return index;
	}
}
