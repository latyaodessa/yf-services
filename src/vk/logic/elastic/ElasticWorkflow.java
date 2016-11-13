package vk.logic.elastic;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;

import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import vk.logic.entities.Post;
import vk.parser.dto.elastic.PostElasticDTO;
import yf.core.PropertiesReslover;
import yf.user.dto.GeneralUserDTO;
import yf.user.dto.fb.FBResponseDTO;
import yf.user.dto.vk.VKResponseDTO;
import yf.user.entities.usersaved.UserSavedPhotos;
import yf.user.entities.usersaved.UserSavedPosts;

public class ElasticWorkflow {
	public static final int TOP_SIZE = 30;
	public static final String EXTERNAL_INDEX_ADDITION = "external-"; 
	
	@Inject
	ElasticSingleton elastic;
	@Inject
	PropertiesReslover properties;
	

	public String checkTag(Post post){
		if(post.getText().contains(properties.get("tag.vk.sets"))){
			return properties.get("elastic.index.sets");
		}
		if(post.getText().contains(properties.get("tag.vk.native"))){
			return properties.get("elastic.index.native");
		}
		if(post.getText().contains(properties.get("tag.vk.soft"))){
			return properties.get("elastic.index.soft");
		}
		if(post.getText().contains(properties.get("tag.vk.black"))){
			return properties.get("elastic.index.black");
		}
		if(post.getText().contains(properties.get("tag.vk.silhouettes"))){
			return properties.get("elastic.index.silhouettes");
		}
		if(post.getText().contains(properties.get("tag.vk.80s90s"))){
			return properties.get("elastic.index.80s90s");
		}
		if(post.getText().contains(properties.get("tag.vk.legs"))){
			return properties.get("elastic.index.legs");
		}
		if(post.getText().contains(properties.get("tag.vk.art"))){
			return properties.get("elastic.index.art");
		}
		return null;
	}
	
	public boolean indexElasticPost(Post post){
		Validate.notNull(post);
		
		String tag = checkTag(post);
		
		if(tag != null){
			return saveItemInIndex(new Index.Builder(post).index(tag).type(properties.get("elastic.type.photo")).id(post.getId().toString()).build());
		}
		
		return false;
	}
	
	public boolean indexEXTERNALElasticPost(Post post, String index){
		Validate.notNull(index);
		Validate.notNull(post);
		
		return saveItemInIndex(new Index.Builder(post).index(EXTERNAL_INDEX_ADDITION + index).type(properties.get("elastic.type.photo")).id(post.getId().toString()).build());
	}
	
	public boolean bulkUpdateIndex(Set<PostElasticDTO> post, String indexTag, String type){
		try {
			Set<Index> indexes = new HashSet<Index>();
			post.forEach(p->{
				indexes.add(new Index.Builder(p).
						id(p.getId().toString())
						.build());
			});
			
			Bulk bulk = new Bulk.Builder()
					.defaultIndex(indexTag)
					.defaultType(type)
	                .addAction(indexes)
	                .build();
			
			elastic.getClient().execute(bulk);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
	
	public boolean indexElasticGeneralUser(GeneralUserDTO generalUser){
		String tag = properties.get("elastic.index.user.general");
			return saveItemInIndex(new Index.Builder(generalUser).index(tag).type(properties.get("elastic.type.user")).id(generalUser.getId().toString()).build());
	}
	
	public boolean indexElasticVKUser(VKResponseDTO vkUser){
		String tag = properties.get("elastic.index.user.vk");
			return saveItemInIndex(new Index.Builder(vkUser).index(tag).type(properties.get("elastic.type.user")).id(vkUser.getId().toString()).build());
	}
	
	public boolean indexElasticFBUser(FBResponseDTO fbResponseDTO){
		String tag = properties.get("elastic.index.user.fb");
			return saveItemInIndex(new Index.Builder(fbResponseDTO).index(tag).type(properties.get("elastic.type.user")).id(fbResponseDTO.getId().toString()).build());
	}
	
	public boolean indexElasticUserSavedPost(UserSavedPosts post){
		String tag = properties.get("elastic.index.user.saved.post");
			return saveItemInIndex(new Index.Builder(post).index(tag).type(properties.get("elastic.type.user")).id(post.getId().toString()).build());
	}
	
	public boolean indexElasticUserSavedPhoto(UserSavedPhotos postDTO){
		String tag = properties.get("elastic.index.user.saved.photo");
			return saveItemInIndex(new Index.Builder(postDTO).index(tag).type(properties.get("elastic.type.user")).id(postDTO.getId().toString()).build());
	}
	
	
	private boolean saveItemInIndex(Index index){
		try {
			elastic.getClient().execute(index);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean updateItemInIndex(Post post){
			String tag = checkTag(post);
			if(tag != null){

				deleteById(tag,post.getId().toString(),properties.get("elastic.type.photo"));
				
				saveItemInIndex(new Index.Builder(post).index(tag).type(properties.get("elastic.type.photo")).id(post.getId().toString()).build());
			
			return true;
			}
		return false;
	}
	
	public void deleteById(String tag, String id, String type){
		try {
			elastic.getClient().execute(new Delete.Builder(id)
			        .index(tag)
			        .type(type)
			        .build());
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public void deleteIndex(String index){
		try {
			elastic.getClient().execute(new Delete.Builder("")
			        .index(index)
			        .build());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	private SearchResult searchNewHitsByIndex(String index, String type){
        SearchResult result = null;
    		try {
    			result = elastic.getClient().execute(new Search.Builder("").addIndex(index).addType(type).setParameter("size", TOP_SIZE).setParameter("sort", "id:desc").build());
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    		return result;
	}
	
	public void getNativeWeeklyTop(){
		
		List<Hit<PostElasticDTO, Void>> postsNative = searchNewHitsByIndex(properties.get("elastic.index.native"), properties.get("elastic.type.photo")).getHits(PostElasticDTO.class);
		List<Hit<PostElasticDTO, Void>> postsSets = searchNewHitsByIndex(properties.get("elastic.index.sets"), properties.get("elastic.type.photo")).getHits(PostElasticDTO.class);

		deleteIndex(properties.get("elastic.index.native.top"));
		deleteIndex(properties.get("elastic.index.sets.top"));

		for(Hit<PostElasticDTO, Void> hit : postsNative){
			Post post = hit.source; 
			saveItemInIndex(new Index.Builder(post).index(properties.get("elastic.index.native.top")).type(properties.get("elastic.type.photo")).id(post.getId().toString()).build());
			}
		
		for(Hit<PostElasticDTO, Void> hit : postsSets){
			Post post = hit.source; 
			saveItemInIndex(new Index.Builder(post).index(properties.get("elastic.index.sets.top")).type(properties.get("elastic.type.photo")).id(post.getId().toString()).build());
			}
		
	}
	

	

}
