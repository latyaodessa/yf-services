package yf.user;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.rest.RestStatus;

import yf.core.PropertiesReslover;
import yf.core.elastic.ElasticToObjectConvertor;
import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.user.dto.UserElasticDTO;
import yf.user.dto.external.fb.FBResponseDTO;
import yf.user.dto.external.vk.VKUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.VKUser;

@Stateless
public class UserWorkflow {
	
	@PersistenceContext
	private EntityManager em;
	@Inject
	UserConverter userConverter;
	@Inject
	ElasticWorkflow elasticWorkflow;
	@Inject
	NativeElasticSingleton nativeElastic;
	@Inject
	ElasticToObjectConvertor elasticToObjectConvertor;
	@Inject
	PropertiesReslover properties;
	
	public UserElasticDTO getUserById(String userId){
		GetResponse res = nativeElastic.getClient().prepareGet(properties.get("elastic.index.user"),
				properties.get("elastic.index.user.type"), userId)
		        .setOperationThreaded(false)
		        .get();
		
		if(!res.isExists()) { return null; }
		
		UserElasticDTO searchResult = elasticToObjectConvertor
								.convertSingleResultToObject(res.getSourceAsString(), UserElasticDTO.class);		

		
		return searchResult;
	}
	public UserSavedPosts getSavedUserPostById(long postId){
		UserSavedPosts userSavedPosts = em.find(UserSavedPosts.class, postId);
		return userSavedPosts !=null ? userSavedPosts : null;

	}
	
	public UserElasticDTO saveVKUser(VKUserDTO vKUserDTO){
		
		VKUser vkUserEntity = userConverter.toVKUsetEntity(vKUserDTO);
		
		if(getUserById(vkUserEntity.getId().toString()) != null){ return null;}

		UserElasticDTO userElasticDto = userConverter.userVKtoUserElastic(vkUserEntity);
		

		IndexResponse response = nativeElastic.getClient()
					.prepareIndex(properties.get("elastic.index.user"),
									properties.get("elastic.index.user.type"), 
									userElasticDto.getId().toString())
					.setSource(elasticWorkflow.objectToSource(userElasticDto))
					.get();
		
		if(response.status() != RestStatus.CREATED){
			return null;
		}
	
                em.persist(vkUserEntity);
                return userElasticDto;
		
		
	}

	public UserElasticDTO saveFBUser(FBResponseDTO fbResponseDTO) {
		FBUser fbUserEntity = userConverter.toFBUserEntity(fbResponseDTO);
		
		if(getUserById(fbUserEntity.getId().toString()) != null){ return null; }
		
		
	UserElasticDTO userElasticDto = userConverter.userFBtoUserElastic(fbUserEntity);
		

		IndexResponse response = nativeElastic.getClient()
					.prepareIndex(properties.get("elastic.index.user"),
									properties.get("elastic.index.user.type"),
									userElasticDto.getId().toString())
					.setSource(elasticWorkflow.objectToSource(userElasticDto))
					.get();
		
		if(response.status() != RestStatus.CREATED){
			return null;
		}
			
            em.persist(fbUserEntity);
            return userElasticDto;
	
	}
	

}
