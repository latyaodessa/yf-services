package yf.dashboard.postphoto;

import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.sort.SortOrder;

import yf.core.PropertiesReslover;
import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.dashboard.postphoto.dto.SavedPostToDashboardDTO;
import yf.elastic.core.NativeElasticSingleton;

public class PostPhotoDashboardService {
	
	@Inject
	PostPhotoDashboardWorkflow postPhotoDashboardWorkflow;
	@Inject
	private NativeElasticSingleton nativeElastic;
	@Inject
	private PropertiesReslover properties;

	
	public void saveNewPostForUser(final SavedPostToDashboardDTO savePostDTO){
	if(isPostDoesntExist(savePostDTO.getUser_id(), savePostDTO.getPost_id())){
		 postPhotoDashboardWorkflow.saveNewPostForUser(savePostDTO);
		}
	}
	
	public boolean isPostDoesntExist(final Long user_id, final Long post_id) {
		return postPhotoDashboardWorkflow.findUserSavedPostByUserIdAndPostId(user_id, post_id)
				.isEmpty();
	}
	
	public PhotoDashboardElasticDTO saveNewPhotoForUser(final SavePhotoDTO photoDto){
		boolean isEmpty = postPhotoDashboardWorkflow.findUserSavedPhotosByUserIdAndPhotoUrl(photoDto.getUser_id(), photoDto.getPhoto_url())
						.isEmpty();
	return isEmpty ? postPhotoDashboardWorkflow.saveNewPhotoForUser(photoDto): null;
	}
	

	public List<PostDashboardElasticDTO> getSavedDashboardPosts(final String user_id, final int from, final int size){
		
		SearchResponse res = nativeElastic.getClient().prepareSearch(properties.get("elastic.index.dashboard.saved.post"))
		        .setTypes(properties.get("elastic.type.dashboard"))
		        .addSort("date", SortOrder.DESC)
		        .setFrom(from).setSize(size).setExplain(true)
		        .setQuery(QueryBuilders.boolQuery().must(QueryBuilders.termQuery("user_id", user_id)))
		        .execute()
		        .actionGet();
		
		return postPhotoDashboardWorkflow.getSavedDashboardPosts(res);
	}
	
	public List<PhotoDashboardElasticDTO> getSavedDashboardPhotos(final String user_id, final int from, final int size){
		
		SearchResponse res = nativeElastic.getClient().prepareSearch(properties.get("elastic.index.dashboard.saved.photo"))
		        .setTypes(properties.get("elastic.type.dashboard"))
		        .addSort("date", SortOrder.DESC)
		        .setFrom(from).setSize(size).setExplain(true)
		        .execute()
		        .actionGet();
		
		return postPhotoDashboardWorkflow.getSavedDashboardPhotos(res);
	}
	public boolean deletePostFromUser(final SavedPostToDashboardDTO dto){
		DeleteResponse response = nativeElastic.getClient().prepareDelete(properties.get("elastic.index.dashboard.saved.post"),
																		properties.get("elastic.type.dashboard"),
																		String.valueOf(dto.getId()))
															.get();
		
		if(response.status() != RestStatus.OK){
			return false;
		}

		boolean isRemoved = postPhotoDashboardWorkflow.deletePostFromUser(dto.getUser_id(), dto.getPost_id());
		
		return isRemoved;
	}
	public boolean deletePhotoFromUser(final PhotoDashboardElasticDTO photoDashboardElasticDTO){
		DeleteResponse response = nativeElastic.getClient().prepareDelete(properties.get("elastic.index.dashboard.saved.photo"), 
																			properties.get("elastic.type.dashboard"),
																			photoDashboardElasticDTO.getId().toString())
															.get();
			
			
			if(response.status() != RestStatus.OK){
				return false;
			}
			
			boolean isRemoved = postPhotoDashboardWorkflow.deletePhotoFromUser(photoDashboardElasticDTO.getUser_id(), photoDashboardElasticDTO.getPhoto_url());
			return isRemoved;
}


	
}
