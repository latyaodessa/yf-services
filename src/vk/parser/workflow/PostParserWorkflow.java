package vk.parser.workflow;

import java.io.IOException;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import vk.logic.elastic.ElasticSingleton;
import vk.logic.elastic.ElasticWorkflow;
import vk.logic.entities.Post;
import vk.parser.dto.PostDTO;
import vk.parser.dto.elastic.PostElasticDTO;
import yf.core.PropertiesReslover;
@Stateless
public class PostParserWorkflow {
	
	
	
	@Inject
	ElasticSingleton elastic;
	@PersistenceContext
	private EntityManager em;
	@Inject
	ParserPostConverter postConverter;
	@Inject
	ElasticWorkflow elasticWorkflow;
	@Inject
	PropertiesReslover properties;

	
	public List<PostDTO> saveNewPostData(List<PostDTO> postDTO){
		for(PostDTO dto : postDTO){
	        	Post post = postConverter.toEntity(dto);
	        	PostElasticDTO postElasticDTO = postConverter.toElasticPostDto(post);
	        	if(elasticWorkflow.indexElasticPost(postElasticDTO) && em.find(Post.class, dto.getId())==null){
	                em.persist(post);
	        	}
		}
		return postDTO;
	}
	
	public List<PostDTO> saveNewEXTERNALPostData(List<PostDTO> postDTO, String index){
		for(PostDTO dto : postDTO){
	        	Post post = postConverter.toEntity(dto);
	        	post.setId(getIdForExternalPost(post));
	        	/// NOW ONLY FOR ART
	        	PostElasticDTO postElasticDTO = postConverter.toEXTERNALPostDto(post);
	        	if(elasticWorkflow.indexEXTERNALElasticPost(postElasticDTO, index) && em.find(Post.class, dto.getId())==null){
	                em.persist(post);
	        	}
		}
		return postDTO;
	}
	
	private Long getIdForExternalPost(Post post){
		return Long.parseLong(post.getFrom_id()+post.getId());
	}
	
	public void saveUpdateNewEntry(List<PostDTO> postDTO){
		for(PostDTO dto : postDTO){
		Post post = postConverter.toEntity(dto);
    	PostElasticDTO postElasticDTO = postConverter.toElasticPostDto(post);
    	if(elasticWorkflow.updateItemInIndex(postElasticDTO)){
            em.merge(post);
    		}
    	}
	}
	
	public void saveUpdateWeeklyTop(){
		elasticWorkflow.getNativeWeeklyTop();
	}
	
	public PostElasticDTO getPostById(String id){
		
		SearchResult result = null;
		try {
			result = elastic.getClient().execute(new Search.Builder("").setParameter("_id", id).build());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		List<Hit<PostElasticDTO, Void>> posts = result.getHits(PostElasticDTO.class);
		
		return posts.get(0).source;

		
	}

}
