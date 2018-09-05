package yf.dashboard.postphoto;

import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.UserVerificationDto;
import yf.user.dto.AuthResponseStatusesEnum;
import yf.user.services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("dashboard")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class PostPhotoDashboardRestImpl {

    @Inject
    private PostPhotoDashboardService postPhotoDashboardService;
    @Inject
    private UserService userService;

    @POST
    @Path("save/post/{publication_id}")
    public Response savePostForUser(@PathParam("publication_id") final Long publicationId, final UserVerificationDto dto) {
        final Boolean isValid = userService.validateToken(dto.getUserId(),
                dto.getToken());


        if (!isValid) {
            return Response.status(403).entity(AuthResponseStatusesEnum.TOKEN_NOT_VALID).build();
        }

        final PostDashboardElasticDTO postDashboardElasticDTO = postPhotoDashboardService.saveNewPublicationForUser(publicationId, dto.getUserId());

        return Response.ok().entity(postDashboardElasticDTO).build();
    }

    @GET
    @Path("isexist/post/{user_id}/{pub_id}")
    public PostDashboardElasticDTO isPublicationEmptyByUser(@PathParam("user_id") final Long user_id,
                                                            @PathParam("pub_id") final Long pubId) {
        return postPhotoDashboardService.getSavedPublication(user_id, pubId);
    }
//TODO
//    @POST
//    @Path("save/photo")
//    public PhotoDashboardElasticDTO savePhotoForUser(SavePhotoDTO photoDTO) {
//        return postPhotoDashboardService.saveNewPhotoForUser(photoDTO);
//    }

    @GET
    @Path("saved/posts/{user_id}/{from}/{size}")
    public List<PostDashboardElasticDTO> getSavedDashboardPosts(@PathParam("user_id") String user_id,
                                                                @PathParam("from") int from,
                                                                @PathParam("size") int size) {
        return postPhotoDashboardService.getSavedDashboardPosts(user_id, from, size);
    }


    @GET
    @Path("saved/photos/{user_id}/{from}/{size}")
    public List<PhotoDashboardElasticDTO> getSavedDashboardPhotos(@PathParam("user_id") String user_id,
                                                                  @PathParam("from") int from,
                                                                  @PathParam("size") int size) {
        return postPhotoDashboardService.getSavedDashboardPhotos(user_id, from, size);
    }

    @POST
    @Path("delete/post/{savedPubId}")
    public Response deletePostFromUser(@PathParam("savedPubId") final Long savedPubId, final UserVerificationDto dto) {

        final Boolean isValid = userService.validateToken(dto.getUserId(),
                dto.getToken());


        if (!isValid) {
            return Response.status(403).entity(AuthResponseStatusesEnum.TOKEN_NOT_VALID).build();
        }

        postPhotoDashboardService.deletePostFromUser(savedPubId);

        return Response.ok().build();
    }
}
