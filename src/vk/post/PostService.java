package vk.post;

import javax.ejb.Stateless;
import javax.inject.Inject;

import vk.parser.workflow.PostWorkflow;

@Stateless
public class PostService {
	@Inject
	private PostWorkflow postWorkflow;
	
	public boolean getAndSaveWeeklyTop(){
		postWorkflow.saveUpdateWeeklyTop();
		return true;
		
	}
}
