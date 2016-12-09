package yf.post.parser.rest.client;

import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;

import yf.post.parser.dto.PostDTO;

public class ParserRestClient {
	
	public List<PostDTO> parseAllPages(long groupid, int firstpage, int lastpage){
		 List<PostDTO> postDTOList = Client.create().resource("http://localhost:9091/trigger/vkparser").path(String.valueOf(groupid)).path(String.valueOf(firstpage)).path(String.valueOf(lastpage))
	                .get(new GenericType<List<PostDTO>>(){});
		return postDTOList;
	}
	public List<PostDTO> checkNewPosts(){
		 List<PostDTO> postDTOList = Client.create().resource("http://localhost:8080/yf-services/rest/vk/newposts/execute")
	                .get(new GenericType<List<PostDTO>>(){});
		return postDTOList;
	}

}
