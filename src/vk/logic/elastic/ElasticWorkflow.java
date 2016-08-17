package vk.logic.elastic;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import io.searchbox.core.Delete;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import vk.logic.entities.Post;

public class ElasticWorkflow {
	private static String SETS_TAG= "#sets@youngfolks";
	private static String NATIVE_TAG = "#native@youngfolks";
	private static String SOFT_TAG = "#soft@youngfolks";
	private static String BACK_TAG = "#спинки@youngfolks";
	private static String SILHOUETTES_TAG = "#silhouettes@youngfolks";
	private static String OLDIES_80s90s_TAG = "#80s90s@youngfolks";
	private static String LEGS_TAG = "#ножки@youngfolks";
	private static String ART_TAG = "#art@youngfolks";
	
	private static String YF_TYPE_PHOTO = "photo";
	
	private static String YF_TOP_NATIVE_INDEX = "yf-native-top";
	private static String YF_TOP_SETS_INDEX = "yf-sets-top";
	
	private static String YF_SETS_INDEX = "yf-photo-sets";
	private static String YF_NATIVE_INDEX = "yf-photo-native";
	private static String YF_SOFT_INDEX = "yf-photo-soft";
	private static String YF_BACK_INDEX = "yf-photo-back";
	private static String YF_SILHOUETTES_INDEX = "yf-photo-silhouettes";
	private static String YF_80S90S_INDEX = "yf-photo-80s90s";
	private static String YF_LEGS_INDEX = "yf-photo-legs";
	private static String YF_ART_INDEX = "yf-photo-art";
	

	private static int TOP_SIZE = 30;
	
	@Inject
	ElasticSingleton elastic;
	

	private String checkTag(Post post){
		if(post.getText().contains(SETS_TAG)){
			return YF_SETS_INDEX;
		}
		if(post.getText().contains(NATIVE_TAG)){
			return YF_NATIVE_INDEX;
		}
		if(post.getText().contains(SOFT_TAG)){
			return YF_SOFT_INDEX;
		}
		if(post.getText().contains(BACK_TAG)){
			return YF_BACK_INDEX;
		}
		if(post.getText().contains(SILHOUETTES_TAG)){
			return YF_SILHOUETTES_INDEX;
		}
		if(post.getText().contains(OLDIES_80s90s_TAG)){
			return YF_80S90S_INDEX;
		}
		if(post.getText().contains(LEGS_TAG)){
			return YF_LEGS_INDEX;
		}
		if(post.getText().contains(ART_TAG)){
			return YF_ART_INDEX;
		}
		return null;
	}
	
	public boolean indexElasticPhoto(Post post){
		String tag = checkTag(post);
		if(tag != null){
			return saveItemInIndex(new Index.Builder(post).index(tag).type(YF_TYPE_PHOTO).id(post.getId().toString()).build());
		}
		
		return false;
		
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
		try {
			String tag = checkTag(post);
			if(tag != null){
				elastic.getClient().execute(new Delete.Builder(post.getId().toString())
		                .index(tag)
		                .type(YF_TYPE_PHOTO)
		                .build());
				
				saveItemInIndex(new Index.Builder(post).index(tag).type(YF_TYPE_PHOTO).id(post.getId().toString()).build());
			
			return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void deleteIndex(String index){
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
		
		List<Hit<Post, Void>> postsNative = searchNewHitsByIndex(YF_NATIVE_INDEX, YF_TYPE_PHOTO).getHits(Post.class);
		List<Hit<Post, Void>> postsSets = searchNewHitsByIndex(YF_SETS_INDEX, YF_TYPE_PHOTO).getHits(Post.class);

		deleteIndex(YF_TOP_NATIVE_INDEX);
		deleteIndex(YF_TOP_SETS_INDEX);

		for(Hit<Post, Void> hit : postsNative){
			Post post = hit.source; 
			saveItemInIndex(new Index.Builder(post).index(YF_TOP_NATIVE_INDEX).type(YF_TYPE_PHOTO).id(post.getId().toString()).build());
			}
		
		for(Hit<Post, Void> hit : postsSets){
			Post post = hit.source; 
			saveItemInIndex(new Index.Builder(post).index(YF_TOP_SETS_INDEX).type(YF_TYPE_PHOTO).id(post.getId().toString()).build());
			}
		
	}
	

}
