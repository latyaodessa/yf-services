package yf.post.parser.workflow;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;

import com.fasterxml.jackson.core.JsonProcessingException;

import yf.core.PropertiesReslover;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;
import yf.elastic.reindex.bulkworkflow.PostBulkWorkflow;
import yf.post.PostService;
import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.post.parser.dto.PostDTO;
@Stateless
public class PostParserWorkflow {
	
	@Inject
	NativeElasticSingleton nativeElasticClient;
	@PersistenceContext
	private EntityManager em;
	@Inject
	ParserPostConverter postConverter;
	@Inject
	PostService postService;
	@Inject
	PropertiesReslover properties;
	@Inject
	PostBulkWorkflow postBulkWorkflow;
	@Inject
	ElasticWorkflow elasticWorkflow;
	
	public static final Logger LOGGER = Logger.getLogger(PostBulkWorkflow.class.getName());

	
	public List<PostDTO> saveNewPostData(List<PostDTO> listPostDTO){
		BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
		bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE); // ??
		
		for(PostDTO dto : listPostDTO){
        		Post post = postConverter.toEntity(dto);

				if(postBulkWorkflow.getIndex(post, postBulkWorkflow.TAG_INDEX) != null){
	        	PostElasticDTO postElasticDTO = postConverter.toElasticPostDto(post);
	        	if(em.find(Post.class, dto.getId())==null){
	                em.persist(post);
					try {
						postBulkWorkflow.addEntityToBulk(postElasticDTO, bulkRequest, BulkOptions.DELETE);
						postBulkWorkflow.addEntityToBulk(postElasticDTO, bulkRequest, BulkOptions.INDEX);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
	        		}
				}
		}
        em.flush();
		em.clear();
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			LOGGER.log(java.util.logging.Level.WARNING, "BULK ERROR");
			}
		
		return listPostDTO;
	}
	
	public void saveUpdateNewEntry(List<PostDTO> postDTO){
		BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
		bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE); // ??
		
		for(PostDTO dto : postDTO){
		Post post = postConverter.toEntity(dto);
		if(postBulkWorkflow.getIndex(post, postBulkWorkflow.TAG_INDEX) != null){

    	PostElasticDTO postElasticDTO = postConverter.toElasticPostDto(post);
            em.merge(post);
            
	        	try {
					postBulkWorkflow.addEntityToBulk(postElasticDTO, bulkRequest, BulkOptions.DELETE);
					postBulkWorkflow.addEntityToBulk(postElasticDTO, bulkRequest, BulkOptions.INDEX);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
		    	}
			}
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			LOGGER.log(java.util.logging.Level.WARNING, "BULK ERROR");
			}
		}
	
	public void saveUpdateWeeklyTop(){
		
		Set <String> indecies = new HashSet<String>(Arrays.asList(properties.get("elastic.index.native.top"),properties.get("elastic.index.sets.top") ));
		elasticWorkflow.deleteIndicies(indecies);

		List<PostElasticDTO> nativePosts = postService.getElasticTopPostsFromTo(properties.get("elastic.index.native"),
																			properties.get("elastic.type.photo"), 0, 10);		
		List<PostElasticDTO> setsPosts = postService.getElasticTopPostsFromTo(properties.get("elastic.index.sets"),
																			properties.get("elastic.type.photo"), 0, 10);
		
		BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
		
		nativePosts.forEach( dto -> {
			try {
				postBulkWorkflow.addEntityToBulkWithCustomIndex(dto, bulkRequest, BulkOptions.INDEX, properties.get("elastic.index.native.top"));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
		
		setsPosts.forEach( dto -> {
			try {
				postBulkWorkflow.addEntityToBulkWithCustomIndex(dto, bulkRequest, BulkOptions.INDEX, properties.get("elastic.index.sets.top"));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			LOGGER.log(java.util.logging.Level.WARNING, "BULK ERROR");
			} else {
				LOGGER.log(java.util.logging.Level.INFO, "WEEKLY TOP EXECUTED. Time: " + bulkResponse.getTook()
																					+ " number of actions: " + bulkRequest.numberOfActions());
			}
		
	}

}
