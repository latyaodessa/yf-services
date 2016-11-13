package yf.post.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import yf.post.PostService;
import yf.post.dto.PostDetailsDTO;
import yf.post.dto.SharedBasicPostDTO;

@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
@Stateless 
public class PostRestImpl {
	
	@Inject
	private PostService postService;
	
	@GET
	@Path("{postId}")
	public PostDetailsDTO getPostDetailsDTO(@PathParam("postId") long postId){
		
//		НЕВЕРНЫЙ РЕЗУЛЬТАТ ВЫДАЕТ ПО ПОИСКУ ПО ID
		
		return postService.getPostDetailsDTO(postId);
	}	
	
	
	@GET
	@Path("/new/{from}/{size}/{index}")
	public List<SharedBasicPostDTO> getNewPostsFromTo(@PathParam("from") int from,
														@PathParam("size") int size, 
														@PathParam("index") String index){
		return postService.getNewPostsFromTo(from, size, index);
		}	
	
}
