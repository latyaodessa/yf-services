package yf.dashboard.postphoto;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;

import yf.core.PropertiesReslover;
import yf.core.elastic.ElasticToObjectConvertor;
import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.dashboard.postphoto.dto.SavePostDTO;
import yf.dashboard.postphoto.entities.UserSavedPhotos;
import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;

public class PostPhotoDashboardWorkflow {
	
	@PersistenceContext
	private EntityManager em;
	@Inject
	private NativeElasticSingleton nativeElastic;
	@Inject
	private ElasticWorkflow elasticWorkflow;
	@Inject
	private PropertiesReslover properties;
	@Inject
	private PostPhotoDashboardConverter converter;
	@Inject
	ElasticToObjectConvertor elasticToObjectConvertor;

	public PostDashboardElasticDTO saveNewPostForUser(final SavePostDTO postDto){
				
			UserSavedPosts entity = converter.savePostDTOtoEntity(postDto);
			em.persist(entity);
			em.flush();
			
			PostDashboardElasticDTO elasticDto = converter.toPostDashboardElasticDTO(entity);
			
			IndexResponse response = nativeElastic.getClient()
					.prepareIndex(properties.get("elastic.index.dashboard.saved.post"),
									properties.get("elastic.type.dashboard"), 
									entity.getId().toString())
					.setSource(elasticWorkflow.objectToSource(elasticDto))
					.get();
			
			if(response.status() == RestStatus.CREATED){
				return elasticDto;
			}

			return null;
	}
	
	public PhotoDashboardElasticDTO saveNewPhotoForUser(final SavePhotoDTO photoDto){
		
		UserSavedPhotos entity = converter.savePhotoDTOtoEntity(photoDto);
		em.persist(entity);
		em.flush();
		
		PhotoDashboardElasticDTO elasticDto = converter.toPhotoDashboardElasticDTO(entity);
		
		IndexResponse response = nativeElastic.getClient()
				.prepareIndex(properties.get("elastic.index.dashboard.saved.photo"),
								properties.get("elastic.type.dashboard"), 
								entity.getId().toString())
				.setSource(elasticWorkflow.objectToSource(elasticDto))
				.get();

		if(response.status() == RestStatus.CREATED){
			return elasticDto;
		}

		return null;
		
}
	public List<PostDashboardElasticDTO> getSavedDashboardPosts(final SearchResponse res) {
		 List<PostDashboardElasticDTO> listDto = new ArrayList<PostDashboardElasticDTO>();

			SearchHit[]  hits = res.getHits().getHits();
			for(SearchHit hit : hits) {
				PostDashboardElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PostDashboardElasticDTO.class); 
				listDto.add(dto);
			}
			return listDto;
	}
	public List<PhotoDashboardElasticDTO> getSavedDashboardPhotos(final SearchResponse res) {
		 List<PhotoDashboardElasticDTO> listDto = new ArrayList<PhotoDashboardElasticDTO>();

			SearchHit[]  hits = res.getHits().getHits();
			for(SearchHit hit : hits) {
				PhotoDashboardElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PhotoDashboardElasticDTO.class); 
				listDto.add(dto);
			}
			return listDto;
	}
	
	public boolean deletePostFromUser(final Long user_id, final Long post_id){
		
		  List <UserSavedPosts> userSavedPosts = findUserSavedPostByUserIdAndPostId(user_id, post_id);
		  
		  if(userSavedPosts == null || userSavedPosts.isEmpty() || userSavedPosts.size() > 1){
			  return false;
		  }
		  
		  for(UserSavedPosts saved : userSavedPosts){
			  em.remove(saved);
		  }
		  em.flush();
		  return true;
	}
	
	public boolean deletePhotoFromUser(final Long user_id, final String photo_url){
		
		  List <UserSavedPhotos> userSavedPhotos = findUserSavedPhotosByUserIdAndPhotoUrl(user_id, photo_url);
		  
		  if(userSavedPhotos == null || userSavedPhotos.isEmpty() || userSavedPhotos.size() > 1){
			  return false;
		  }
		  
		  for(UserSavedPhotos saved : userSavedPhotos){
			  em.remove(saved);
		  }
		  em.flush();
		  return true;
	}
	
	public List <UserSavedPosts> findUserSavedPostByUserIdAndPostId(Long user_id, Long post_id){
		  TypedQuery <UserSavedPosts> query = em.createNamedQuery(UserSavedPosts.QUERY_FIND_SAVED_POST_AND_BY_USER_ID, UserSavedPosts.class)
				  .setParameter("user_id", user_id).setParameter("post_id", post_id);
		
		  return query.getResultList();
	}
	
	public List <UserSavedPhotos> findUserSavedPhotosByUserIdAndPhotoUrl(Long user_id, String photo_url){
		  TypedQuery <UserSavedPhotos> query = em.createNamedQuery(UserSavedPhotos.QUERY_FIND_SAVED_PHOTO_AND_BY_USER_ID, UserSavedPhotos.class)
				  .setParameter("user_id", user_id).setParameter("photo_url", photo_url);
		
		  return query.getResultList();
	}
}
