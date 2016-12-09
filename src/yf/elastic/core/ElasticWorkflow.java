package yf.elastic.core;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.apache.commons.lang3.Validate;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.IndicesAdminClient;

import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import yf.core.PropertiesReslover;
import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.user.dto.GeneralUserDTO;
import yf.user.dto.external.fb.FBResponseDTO;
import yf.user.dto.external.vk.VKResponseDTO;
import yf.user.entities.usersaved.UserSavedPhotos;
import yf.user.entities.usersaved.UserSavedPosts;

public class ElasticWorkflow {
	
    private static final Logger LOG = Logger.getLogger(ElasticWorkflow.class.getName());

	public static final int TOP_SIZE = 30;
	public static final String EXTERNAL_INDEX_ADDITION = "external-"; 
	
	
	@Inject
	ElasticSingleton elastic;
	@Inject
	PropertiesReslover properties;
	@Inject
	NativeElasticSingleton nativeElasticClient;

    
    public String getDocumentId(final Long id) {
        return String.valueOf(id);
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
	
	public void deleteIndicies(Map <String, String> tag_indecies){	
		tag_indecies.entrySet().forEach( i -> {
				deleteIndex(i.getValue());
		});
	}
	public void deleteIndicies(Set <String> indecies){
		indecies.forEach( i -> {
				deleteIndex(i);
		});
	}

	
	public void deleteIndex(String index){
		
		IndicesAdminClient adminClinet = nativeElasticClient.getClient()
				.admin().indices();
		
		if(isIndexExist(adminClinet, index)){
			DeleteIndexResponse delete = nativeElasticClient.getClient()
					.admin()
					.indices()
					.delete(new DeleteIndexRequest(index))
					.actionGet();

				if (!delete.isAcknowledged()) {
				LOG.log(Level.SEVERE,"Index wasn't deleted");
					}
		}
	}
	
	public boolean isIndexExist(final IndicesAdminClient adminClinet,final String index){
		return adminClinet.prepareExists(index).execute().actionGet().isExists();
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
