package yf.user.rest;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;

import yf.user.dto.VKUserDTO;

public class UserRestClient {
	public static final String VK_HOST = "https://api.vk.com/method/";
	public static final String VK_METHOD_GET_USERS = "users.get?";
	public static final String VK_USER_IDS = "user_ids=";
	public static final String VK_REQUIRED_FIELDS = "&fields=sex,nickname,maiden_name,domain,bdate,city,country,photo_50,photo_100,photo_200_orig,photo_200,photo_400_orig,mobile_phone,home_phone,site,verified,followers_count,university_name,interests,music,activities,movies,books,games,about,quotes";
	public static final String VK_NAME_CASE_NOM = "&name_case=Nom";
	public static final String VK_API_VERSION_5_53 = "&v=5.53";
	
	public VKUserDTO getVKUserDetails(long userId){
	
					VKUserDTO dto = Client.create().
						resource(userDetailsURI(userId)).accept(MediaType.APPLICATION_JSON)
						.get(new GenericType<VKUserDTO>(){});
				
		return dto;
	}
	
	private String userDetailsURI(long userId){
		return VK_HOST + VK_METHOD_GET_USERS + VK_USER_IDS + userId + VK_REQUIRED_FIELDS + VK_NAME_CASE_NOM + VK_API_VERSION_5_53;
	}
}
