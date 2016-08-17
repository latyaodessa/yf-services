package vk.parser.rest.client;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;

import vk.parser.dto.PostDTO;

public class ParserRestClient {
	
	public List<PostDTO> parseAllPages(int firstpage, int lastpage){
		 List<PostDTO> postDTOList = Client.create().resource("http://localhost:9091/trigger/vkparser/26020797").path(String.valueOf(firstpage)).path(String.valueOf(lastpage))
	                .get(new GenericType<List<PostDTO>>(){});
		return postDTOList;
	}
	
	public List<PostDTO> checkNewPosts(){
		 List<PostDTO> postDTOList = Client.create().resource("http://localhost:8080/yf-services/rest/vk/newposts/execute")
	                .get(new GenericType<List<PostDTO>>(){});
		return postDTOList;
	}

}
