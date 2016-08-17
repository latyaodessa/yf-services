package vk.parser.workflow;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import vk.logic.elastic.ElasticWorkflow;
import vk.logic.entities.Post;
import vk.parser.dto.PostDTO;
@Stateless
public class PostWorkflow {
	
	
	
	@PersistenceContext
	private EntityManager em;
	@Inject
	PostConverter postConverter;
	@Inject
	ElasticWorkflow elasticWorkflow;


	
	public List<PostDTO> saveNewPostData(List<PostDTO> postDTO){
		for(PostDTO dto : postDTO){
	        	Post post = postConverter.toEntity(dto);
	        	if(elasticWorkflow.indexElasticPhoto(post) && em.find(Post.class, dto.getId())==null){
	                em.persist(post);
	        	}
		}
		return postDTO;
	}
	
	public void saveUpdateNewEntry(List<PostDTO> postDTO){
		for(PostDTO dto : postDTO){
		Post post = postConverter.toEntity(dto);
    	if(elasticWorkflow.updateItemInIndex(post)){
            em.merge(post);
	}
    	}
	}
	
	public void saveUpdateWeeklyTop(){
		elasticWorkflow.getNativeWeeklyTop();
	}

}
