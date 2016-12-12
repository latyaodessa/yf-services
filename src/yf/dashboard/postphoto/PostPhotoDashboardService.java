package yf.dashboard.postphoto;

import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.sort.SortOrder;

import yf.core.PropertiesReslover;
import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.dashboard.postphoto.dto.SavePostDTO;
import yf.elastic.core.NativeElasticSingleton;

public class PostPhotoDashboardService {
	
	@Inject
	PostPhotoDashboardWorkflow postPhotoDashboardWorkflow;
	@Inject
	private NativeElasticSingleton nativeElastic;
	@Inject
	private PropertiesReslover properties;

	
	public PostDashboardElasticDTO saveNewPostForUser(final SavePostDTO postDto){
		boolean isEmpty = postPhotoDashboardWorkflow.findUserSavedPostByUserIdAndPostId(postDto.getUser_id(), postDto.getPost_id())
						.isEmpty();
	return isEmpty ? postPhotoDashboardWorkflow.saveNewPostForUser(postDto) : null;
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
	public boolean deletePostFromUser(final PostDashboardElasticDTO postDashboardElasticDTO){
		DeleteResponse response = nativeElastic.getClient().prepareDelete(properties.get("elastic.index.dashboard.saved.post"),
																		properties.get("elastic.type.dashboard"),
																		postDashboardElasticDTO.getId().toString())
															.get();
		
		if(response.status() != RestStatus.OK){
			return false;
		}

		boolean isRemoved = postPhotoDashboardWorkflow.deletePostFromUser(postDashboardElasticDTO.getUser_id(), postDashboardElasticDTO.getPost_id());
		
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
