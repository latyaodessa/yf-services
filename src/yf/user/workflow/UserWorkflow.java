package yf.user.workflow;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import vk.logic.elastic.ElasticWorkflow;
import yf.core.PropertiesReslover;
import yf.user.dto.GeneralUserDTO;
import yf.user.dto.fb.FBResponseDTO;
import yf.user.dto.save.UserPhotoSaveDataDTO;
import yf.user.dto.save.UserPostSaveDataDTO;
import yf.user.dto.vk.VKResponseDTO;
import yf.user.dto.vk.VKUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.VKUser;
import yf.user.entities.usersaved.UserSavedPhotos;
import yf.user.entities.usersaved.UserSavedPosts;

@Stateless
public class UserWorkflow {
	
	@PersistenceContext
	private EntityManager em;
	@Inject
	UserConverter userConverter;
	@Inject
	ElasticWorkflow elasticWorkflow;
	@Inject
	PropertiesReslover properties;
	
	public GeneralUserDTO getUserById(long userId){
		User user = em.find(User.class, userId);
		return user !=null ? userConverter.userEntityToGeneralUserDTO(user) : null;
	}
	public UserSavedPosts getSavedUserPostById(long postId){
		UserSavedPosts userSavedPosts = em.find(UserSavedPosts.class, postId);
		return userSavedPosts !=null ? userSavedPosts : null;

	}
	
	public VKResponseDTO saveVKUser(VKUserDTO vKUserDTO){
		
		VKUser vkUserEntity = userConverter.toVKUsetEntity(vKUserDTO);
	
			if(getUserById(vkUserEntity.getId()) == null){
				
				elasticWorkflow.indexElasticGeneralUser(userConverter.userEntityToGeneralUserDTO(vkUserEntity));
				elasticWorkflow.indexElasticVKUser(vKUserDTO.getResponse().get(0));
                em.persist(vkUserEntity);
                return vKUserDTO.getResponse().get(0);
		}
		
		return null;
	}

	public FBResponseDTO saveFBUser(FBResponseDTO fbResponseDTO) {
		FBUser fbUserEntity = userConverter.toFBUserEntity(fbResponseDTO);
		
		if(getUserById(fbUserEntity.getId()) == null){
			
			elasticWorkflow.indexElasticGeneralUser(userConverter.userEntityToGeneralUserDTO(fbUserEntity));
			elasticWorkflow.indexElasticFBUser(fbResponseDTO);
            em.persist(fbUserEntity);
            return fbResponseDTO;
	}
	
		return null;
	}
	
	public boolean isPostAlreadySavedToUser(long user_id, long post_id){
		  List <UserSavedPosts> userSavedPosts = findUserSavedPostByUserIdAndPostId(user_id, post_id);
		  return !userSavedPosts.isEmpty();
	}
	public boolean isPhotoAlreadySavedToUser(long user_id, String photo_url){
		  List <UserSavedPhotos> userSavedPosts = findUserSavedPhotosByUserIdAndPhotoUrl(user_id, photo_url);
		  return !userSavedPosts.isEmpty();
	}
	public UserPostSaveDataDTO saveNewPostForUser(UserPostSaveDataDTO post){
		
		User user = em.find(User.class, post.getUser_id());

		if(user != null && !isPostAlreadySavedToUser(post.getUser_id(), post.getPost_id())){
			UserSavedPosts entity = userConverter.savePostDTOtoEntity(post);
			em.persist(entity);
			em.flush();
			elasticWorkflow.indexElasticUserSavedPost(entity);

			return post;
		}
		
		return null;	
	}
	
	public UserPhotoSaveDataDTO saveNewPhotoForUser(UserPhotoSaveDataDTO post){
		
		User user = em.find(User.class, post.getUser_id());

		if(user != null && !isPhotoAlreadySavedToUser(post.getUser_id(), post.getPhoto_url())){
			UserSavedPhotos entity = userConverter.savePhotoDTOtoEntity(post);
			em.persist(entity);
			em.flush();
			elasticWorkflow.indexElasticUserSavedPhoto(entity);

			return post;
		}
		
		return null;	
	}
	
	public List <UserSavedPosts> deletePostFromUser(Long user_id, Long post_id){
		
		  List <UserSavedPosts> userSavedPosts = findUserSavedPostByUserIdAndPostId(user_id, post_id);
		  
		  if(userSavedPosts == null || userSavedPosts.isEmpty()){
			  return null;
		  }
		  
		  for(UserSavedPosts saved : userSavedPosts){
			  em.remove(saved);
			  elasticWorkflow.deleteById(properties.get("elastic.index.user.saved.post"), saved.getId().toString(), properties.get("elastic.type.user"));
		  }
		  return userSavedPosts;
	}
	
	
	public UserSavedPhotos deletePhotoFromUser(Long user_id, Long photo_id){
		
			UserSavedPhotos userSavedPhoto = em.find(UserSavedPhotos.class, photo_id);
		  
		  if(userSavedPhoto == null ){
			  return null;
		  }
		  
			  em.remove(userSavedPhoto);
			  elasticWorkflow.deleteById(properties.get("elastic.index.user.saved.photo"), userSavedPhoto.getId().toString(), properties.get("elastic.type.user"));
		  
		  return userSavedPhoto;
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
