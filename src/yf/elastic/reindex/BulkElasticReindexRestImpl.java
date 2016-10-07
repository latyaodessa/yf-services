package yf.elastic.reindex;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("reindex")
@Produces(MediaType.APPLICATION_JSON)
@Stateless 
public class BulkElasticReindexRestImpl {
	
	@Inject
	ReindexWorkflow reindexWorkflow;
	
	@GET
	@Path("vk/posts")
	public Boolean postsBulkReindex(){
		return reindexWorkflow.reindexPosts();
		 
	}
	
}
