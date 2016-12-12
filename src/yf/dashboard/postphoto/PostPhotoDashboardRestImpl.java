package yf.dashboard.postphoto;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.dashboard.postphoto.dto.SavePostDTO;


@Path("dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Stateless 
public class PostPhotoDashboardRestImpl {
	
	@Inject 
	private PostPhotoDashboardService postPhotoDashboardService;
	
	@POST
	@Path("save/post")
	public PostDashboardElasticDTO savePostForUser(SavePostDTO post){
		return postPhotoDashboardService.saveNewPostForUser(post);
	}
	
	@POST
	@Path("save/photo")
	public PhotoDashboardElasticDTO savePhotoForUser(SavePhotoDTO photoDTO){
		return postPhotoDashboardService.saveNewPhotoForUser(photoDTO);
	}
	
	@GET
	@Path("saved/posts/{user_id}/{from}/{size}")
	public List<PostDashboardElasticDTO> getSavedDashboardPosts(@PathParam("user_id") String user_id,
														@PathParam("from") int from,
														@PathParam("size") int size){
		return postPhotoDashboardService.getSavedDashboardPosts(user_id, from, size);
	}
	
	@GET
	@Path("saved/photos/{user_id}/{from}/{size}")
	public List<PhotoDashboardElasticDTO> getSavedDashboardPhotos(@PathParam("user_id") String user_id,
														@PathParam("from") int from,
														@PathParam("size") int size){
		return postPhotoDashboardService.getSavedDashboardPhotos(user_id, from, size);
	}
	
	@POST
	@Path("delete/post")
	public boolean deletePostFromUser(final PostDashboardElasticDTO postDashboardElasticDTO){
		return postPhotoDashboardService.deletePostFromUser(postDashboardElasticDTO);
	}
	
	@POST
	@Path("delete/photo")
	public boolean deletePhotoFromUser(final PhotoDashboardElasticDTO photoDashboardElasticDTO){
		return postPhotoDashboardService.deletePhotoFromUser(photoDashboardElasticDTO);
	}

}
