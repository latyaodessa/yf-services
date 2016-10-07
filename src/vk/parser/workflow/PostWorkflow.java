package vk.parser.workflow;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import vk.logic.elastic.ElasticWorkflow;
import vk.logic.entities.Post;
import vk.parser.dto.PostDTO;
import vk.parser.dto.elastic.PostElasticDTO;
import yf.core.PropertiesReslover;
@Stateless
public class PostWorkflow {
	
	
	
	@PersistenceContext
	private EntityManager em;
	@Inject
	PostConverter postConverter;
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
	
	public List<PostDTO> saveExteranalPostData(List<PostDTO> postDTO, String index){
//		for(PostDTO dto : postDTO){
//	        	Post post = defineConverter(postConverter.toEntity(dto), index);
//	        	
//	        	if(elasticWorkflow.indexElasticPhoto(postElasticDTO) && em.find(Post.class, dto.getId())==null){
//	                em.persist(post);
//	        	}
//		}
		return postDTO;
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
//	
//	public Long countPostRows(){
//		
//		CriteriaQuery<Long> cq = em.getCriteriaBuilder()
//								.createQuery(Long.class);
//		cq.select(em.getCriteriaBuilder().count(cq.from(Post.class)));	
//		return em.createQuery(cq).getSingleResult();
//	}
	
	


}
