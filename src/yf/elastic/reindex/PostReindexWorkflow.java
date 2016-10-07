package yf.elastic.reindex;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import vk.logic.elastic.ElasticWorkflow;
import vk.logic.entities.Post;
import vk.parser.dto.elastic.PostElasticDTO;
import vk.parser.workflow.PostConverter;
import yf.core.PropertiesReslover;

public class PostReindexWorkflow {
	@Inject
	private PostConverter postConverter;
	@Inject
	private PropertiesReslover properties;
	@Inject
	ElasticWorkflow elasticWorkflow;
	public static final Logger LOGGER = Logger.getLogger(PostReindexWorkflow.class.getName());

	
	private Set<PostElasticDTO> yf_native;
	private Set<PostElasticDTO> yf_sets;
	private Set<PostElasticDTO> yf_soft;
	private Set<PostElasticDTO> yf_black;
	private Set<PostElasticDTO> yf_silhouettes;
	private Set<PostElasticDTO> yf_80s90s;
	private Set<PostElasticDTO> yf_legs;
	private Set<PostElasticDTO> yf_art;

	@PostConstruct
	private void initLists(){
		yf_native = new HashSet<PostElasticDTO>();
		yf_sets = new HashSet<PostElasticDTO>();
		yf_soft = new HashSet<PostElasticDTO>();
		yf_black = new HashSet<PostElasticDTO>();
		yf_silhouettes = new HashSet<PostElasticDTO>();
		yf_80s90s = new HashSet<PostElasticDTO>();
		yf_legs = new HashSet<PostElasticDTO>();
		yf_art = new HashSet<PostElasticDTO>();

	}
	public void execute(List<Post> posts){
		
		prepareIndexes();
		
		for(Post p : posts){
            PostElasticDTO postElasticDto = postConverter.toElasticPostDto(p);
			saveToListByTag(postElasticDto);
		};
		
		postBulkUpdate();
	}
	
	private void postBulkUpdate(){
		elasticWorkflow.bulkUpdateIndex(yf_native, properties.get("elastic.index.native"), properties.get("elastic.type.photo"));
		elasticWorkflow.bulkUpdateIndex(yf_sets, properties.get("elastic.index.sets"), properties.get("elastic.type.photo"));
		elasticWorkflow.bulkUpdateIndex(yf_soft, properties.get("elastic.index.soft"), properties.get("elastic.type.photo"));
		elasticWorkflow.bulkUpdateIndex(yf_black, properties.get("elastic.index.black"), properties.get("elastic.type.photo"));
		elasticWorkflow.bulkUpdateIndex(yf_silhouettes, properties.get("elastic.index.silhouettes"), properties.get("elastic.type.photo"));
		elasticWorkflow.bulkUpdateIndex(yf_80s90s, properties.get("elastic.index.80s90s"), properties.get("elastic.type.photo"));
		elasticWorkflow.bulkUpdateIndex(yf_legs, properties.get("elastic.index.legs"), properties.get("elastic.type.photo"));
		elasticWorkflow.bulkUpdateIndex(yf_art, properties.get("elastic.index.art"), properties.get("elastic.type.photo"));
	}
	
	
	private void saveToListByTag(PostElasticDTO post){
		if(post.getText().contains(properties.get("tag.vk.sets"))){
			 yf_sets.add(post);
		}
		if(post.getText().contains(properties.get("tag.vk.native"))){
			yf_native.add(post);
		}
		if(post.getText().contains(properties.get("tag.vk.soft"))){
			yf_soft.add(post);
		}
		if(post.getText().contains(properties.get("tag.vk.black"))){
			yf_black.add(post);
		}
		if(post.getText().contains(properties.get("tag.vk.silhouettes"))){
			yf_silhouettes.add(post);
		}
		if(post.getText().contains(properties.get("tag.vk.80s90s"))){
			yf_80s90s.add(post);
		}
		if(post.getText().contains(properties.get("tag.vk.legs"))){
			yf_legs.add(post);
		}
		if(post.getText().contains(properties.get("tag.vk.art"))){
			yf_art.add(post);
		}
	}
	
	private void prepareIndexes(){
			preparePostsIndex(properties.get("elastic.index.native"));
			preparePostsIndex(properties.get("elastic.index.sets"));
			preparePostsIndex(properties.get("elastic.index.soft"));
			preparePostsIndex(properties.get("elastic.index.black"));
			preparePostsIndex(properties.get("elastic.index.silhouettes"));
			preparePostsIndex(properties.get("elastic.index.80s90s"));
			preparePostsIndex(properties.get("elastic.index.legs"));
			preparePostsIndex(properties.get("elastic.index.art"));
}
	private void preparePostsIndex(String elastic_index){
		elasticWorkflow.deleteIndex(elastic_index);
	}

}
