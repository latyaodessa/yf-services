package vk.post.rest;

import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/post/vk")
@Produces(MediaType.APPLICATION_JSON)
@Stateless 
public class VKPostRestImpl {
	
//	@GET
//	@Path("parseall/{firstpage}/{lastpage}")
//	public List<PostDTO> parseAll(@PathParam("firstpage") int firstpage, @PathParam("lastpage") int lastpage){
//		return parserService.triggerPostParser(firstpage, lastpage);
//	}
}
