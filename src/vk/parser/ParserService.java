package vk.parser;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import vk.parser.dto.PostDTO;
import vk.parser.rest.client.ParserRestClient;
import vk.parser.workflow.PostWorkflow;

@Stateless
public class ParserService {
	@Inject
	private PostWorkflow postWorkflow;
	@Inject 
	private ParserRestClient parserRestClient;
	
	public List<PostDTO> triggerPostParser(int firstpage, int lastpage){
		List<PostDTO> postDTOList = new ArrayList<PostDTO>();
//		for(int i = lastpage;i-100>=firstpage;i-=100){
//		postDTOList.addAll(parserRestClient.parseAllPages(i-100, i));
//		}
		for(int i = firstpage;i+100<=lastpage;i+=100){
		postDTOList.addAll(parserRestClient.parseAllPages(i, i+100));
		}
		postWorkflow.saveNewPostData(postDTOList);
		return postDTOList;
		
	}
	
	
	public List<PostDTO> triggerPostParserForNewPosts(){
		List<PostDTO> postDTOList = new ArrayList<PostDTO>();
		postDTOList = parserRestClient.parseAllPages(0,100);
		postWorkflow.saveUpdateNewEntry(postDTOList);
		return postDTOList;
		
	}
	

}
