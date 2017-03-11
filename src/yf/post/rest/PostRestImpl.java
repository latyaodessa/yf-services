package yf.post.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
	public PostDetailsDTO getPostDetailsDTO(@PathParam("postId") String postId){		
		return postService.getPostDetailsDTO(postId);
	}	
	
	@GET
	@Path("/new/{index}/{type}/{from}/{size}")
	public List<SharedBasicPostDTO> getNewPostsFromTo(@PathParam("index") String index,
														@PathParam("type") String type,
														@PathParam("from") int from,
														@PathParam("size") int size){
		return postService.getNewPostsFromTo(index, type,from, size);
		}
	
	@GET
	@Path("/top/{index}/{type}/{from}/{size}")
	public List<SharedBasicPostDTO> getTopPostsFromTo(@PathParam("index") String index,
														@PathParam("type") String type,
														@PathParam("from") int from,
														@PathParam("size") int size){
		return postService.getTopPostsFromTo(index, type,from, size);
	}
	
	@POST
	@Path("search")
	public List<SharedBasicPostDTO> createFBUser(@QueryParam ("query") String query){
		return postService.searchPosts(query);
	}
	
}
