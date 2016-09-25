package yf.user.workflow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

import yf.user.dto.GeneralUserDTO;
import yf.user.dto.fb.FBResponseDTO;
import yf.user.dto.save.UserPhotoSaveDataDTO;
import yf.user.dto.save.UserPostSaveDataDTO;
import yf.user.dto.vk.VKCityCountryDTO;
import yf.user.dto.vk.VKResponseDTO;
import yf.user.dto.vk.VKUserDTO;
import yf.user.entities.FBUser;
import yf.user.entities.User;
import yf.user.entities.VKUser;
import yf.user.entities.usersaved.UserSavedPhotos;
import yf.user.entities.usersaved.UserSavedPosts;

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
			vkUserEntity.setCity(Optional.ofNullable(resDto).map(VKResponseDTO::getCity).map(VKCityCountryDTO::getTitle).orElse(null));
			vkUserEntity.setCountry(Optional.ofNullable(resDto).map(VKResponseDTO::getCountry).map(VKCityCountryDTO::getTitle).orElse(null));


		
		return vkUserEntity;
		
	}

	public FBUser toFBUserEntity(FBResponseDTO fbResponseDTO) {
		FBUser entity = new FBUser();
		
		entity.setId(fbResponseDTO.getId());
		entity.setPassword(null);
		entity.setEmail(fbResponseDTO.getEmail());
		entity.setUserAuthorized(false);
		entity.setType("fb");
		
		entity.setFirst_name(fbResponseDTO.getFirst_name());
		entity.setLast_name(fbResponseDTO.getLast_name());
		entity.setLocation(fbResponseDTO.getLocation());
		entity.setLocale(fbResponseDTO.getLocale());
		entity.setGender(fbResponseDTO.getGender());
		entity.setBirthday(fbResponseDTO.getBirthday());
		entity.setHometown(fbResponseDTO.getHometown());
		entity.setLanguages(fbResponseDTO.getLanguages());
		entity.setHometown(fbResponseDTO.getHometown());
		entity.setSmall_pic(fbResponseDTO.getPicture().getData().getUrl());

		return entity;
	}
	
	public UserSavedPosts savePostDTOtoEntity(UserPostSaveDataDTO dto){
		UserSavedPosts entity = new UserSavedPosts();
		
		
		entity.setDate(new Date());
		entity.setPost_id(dto.getPost_id());
		entity.setPost_type(dto.getPost_type());
		entity.setUser_id(dto.getUser_id());
		
		return entity;
		
	}
	
	public UserSavedPhotos savePhotoDTOtoEntity(UserPhotoSaveDataDTO dto){
		UserSavedPhotos entity = new UserSavedPhotos();
		
		entity.setDate(new Date());
		entity.setPhoto_url(dto.getPhoto_url());
		entity.setNote(dto.getNote());
		entity.setUser_id(dto.getUser_id());
		
		entity.setPost_id(dto.getPost_id());
		entity.setMd(dto.getMd());
		entity.setPh(dto.getPh());
		
		return entity;
		
	}
	

}
