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
	
	private static final long YF_GROUP_ID = 26020797;
	
	public List<PostDTO> triggerPostParser(int firstpage, int lastpage){
		List<PostDTO> postDTOList = new ArrayList<PostDTO>();
		
		for(int i = firstpage;i+100<=lastpage;i+=100){
		postDTOList.addAll(parserRestClient.parseAllPages(YF_GROUP_ID,i, i+100));
		}
		postWorkflow.saveNewPostData(postDTOList);
		return postDTOList;
		
	}
	
	public List<PostDTO> triggerExternalPostParser(int groupid, int firstpage, int lastpage, String index){
		List<PostDTO> postDTOList = new ArrayList<PostDTO>();
		
		for(int i = firstpage;i+100<=lastpage;i+=100){
		postDTOList.addAll(parserRestClient.parseAllPages(groupid, i, i+100));
		}
		postWorkflow.saveNewEXTERNALPostData(postDTOList, index);
		return postDTOList;
		
	}
	
	
	public List<PostDTO> triggerPostParserForNewPosts(){
		List<PostDTO> postDTOList = new ArrayList<PostDTO>();
		postDTOList = parserRestClient.parseAllPages(YF_GROUP_ID, 0,100);
		postWorkflow.saveUpdateNewEntry(postDTOList);
		return postDTOList;
		
	}
	

}
