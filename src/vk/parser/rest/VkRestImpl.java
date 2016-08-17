package vk.parser.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import vk.parser.ParserService;
import vk.parser.dto.PostDTO;
import vk.post.PostService;

@Path("/vk")
@Produces(MediaType.APPLICATION_JSON)
@Stateless 
public class VkRestImpl {
		@Inject
	 	ParserService parserService;
		@Inject
	 	PostService postService;

//		@Inject
//		RestScheduler restScheduler;
		@PersistenceContext
		private EntityManager em;
		
		@GET
		@Path("alive")
		@Produces(MediaType.APPLICATION_OCTET_STREAM)
		public String aliveMethod(){
			return "Yes baby, i'm alive";
		}
		@GET
		@Path("parseall/{firstpage}/{lastpage}")
		public List<PostDTO> parseAll(@PathParam("firstpage") int firstpage, @PathParam("lastpage") int lastpage){
			return parserService.triggerPostParser(firstpage, lastpage);
		}
		@GET
		@Path("newposts/{command}")
		public List<PostDTO> checkNewPostMethod(@PathParam("command") String command){
			if("execute".equals(command)){
			return parserService.triggerPostParserForNewPosts();
			}
			if("top".equals(command)){
				postService.getAndSaveWeeklyTop();
				return null;
				}
			return null;
		}
}
