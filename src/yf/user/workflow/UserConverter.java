package yf.user.workflow;

import java.util.ArrayList;
import java.util.List;

import vk.parser.rest.VkRestImpl;
import yf.user.dto.GeneralUserDTO;
import yf.user.dto.VKResponseDTO;
import yf.user.dto.VKUserDTO;
import yf.user.entities.User;
import yf.user.entities.VKUser;

public class UserConverter {
	
	public GeneralUserDTO userEntityToGeneralUserDTO(User user){
		GeneralUserDTO dto = new GeneralUserDTO();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setType(user.getType());
		return dto;
	}
	
	public VKUser toVKUsetEntity(VKUserDTO dto){
		VKUser vkUserEntity = new VKUser();
		
		VKResponseDTO resDto = dto.getResponse().get(0);
		
	
			
			vkUserEntity.setId(resDto.getId());
			vkUserEntity.setPassword(null);
			vkUserEntity.setEmail(null);
			vkUserEntity.setUserAuthorized(false);
			vkUserEntity.setType("vk");
			
			vkUserEntity.setFirst_name(resDto.getFirst_name());
			vkUserEntity.setLast_name(resDto.getLast_name());
			vkUserEntity.setSex(resDto.getSex());
			vkUserEntity.setNickname(resDto.getNickname());
			vkUserEntity.setMaiden_name(resDto.getMaiden_name());
			vkUserEntity.setDomain(resDto.getDomain());
			vkUserEntity.setBdate(resDto.getBdate());
			vkUserEntity.setPhoto_50(resDto.getPhoto_50());
			vkUserEntity.setPhoto_100(resDto.getPhoto_100());
			vkUserEntity.setPhoto_200(resDto.getPhoto_200());
			vkUserEntity.setPhoto_200_orig(resDto.getPhoto_200_orig());
			vkUserEntity.setPhoto_400_orig(resDto.getPhoto_400_orig());
			vkUserEntity.setMobile_phone(resDto.getMobile_phone());
			vkUserEntity.setHome_phone(resDto.getHome_phone());
			vkUserEntity.setSite(resDto.getSite());
			vkUserEntity.setVerified(resDto.getVerified());
			vkUserEntity.setFollowers_count(resDto.getFollowers_count());
			vkUserEntity.setUniversity_name(resDto.getUniversity_name());
			vkUserEntity.setInterests(resDto.getInterests());
			vkUserEntity.setMusic(resDto.getMusic());
			vkUserEntity.setActivities(resDto.getActivities());
			vkUserEntity.setMovies(resDto.getMovies());
			vkUserEntity.setBooks(resDto.getBooks());
			vkUserEntity.setGames(resDto.getGames());
			vkUserEntity.setAbout(resDto.getAbout());
			vkUserEntity.setQuotes(resDto.getQuotes());
			vkUserEntity.setCity(resDto.getCity().getTitle());
			vkUserEntity.setCountry(resDto.getCountry().getTitle());
			
			


		
		return vkUserEntity;
		
	}
	

}
