package yf.user.workflow;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import vk.logic.elastic.ElasticWorkflow;
import yf.user.dto.GeneralUserDTO;
import yf.user.dto.VKResponseDTO;
import yf.user.dto.VKUserDTO;
import yf.user.entities.User;
import yf.user.entities.VKUser;

@Stateless
public class UserWorkflow {
	
	@PersistenceContext
	private EntityManager em;
	@Inject
	UserConverter userConverter;
	@Inject
	ElasticWorkflow elasticWorkflow;
	
	public GeneralUserDTO getUserById(long userId){
		User user = em.find(User.class, userId);
		return user !=null ? userConverter.userEntityToGeneralUserDTO(user) : null;
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

}
