package yf.publication.rest;

import yf.post.dto.SharedBasicPostDTO;
import yf.publication.PublicationService;
import yf.publication.dtos.PublicationElasticDTO;
import yf.publication.dtos.PublicationPicturesDTO;
import yf.publication.dtos.PublicationTypeEnum;
import yf.publication.entities.PublicationPictures;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("publication")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class PublicationRestImpl {

    @Inject
    private PublicationService publicationService;

    @GET
    @Path("{publication_id}")
    public PublicationElasticDTO getPulicationById(@PathParam("publication_id") final String publicationId) {
        return publicationService.getPublicationById(publicationId);
    }

    @GET
    @Path("link/{link}")
    public PublicationElasticDTO getPulicationByLink(@PathParam("link") final String link) {
        return publicationService.getPublicationByLink(link);
    }

    @GET
    @Path("get/{type}/{from}/{size}")
    public List<SharedBasicPostDTO> getPublicationsByTypeFromTo(@PathParam("type") final PublicationTypeEnum typeEnum,
                                                                @PathParam("from") final int from,
                                                                @PathParam("size") final int size) {
        return publicationService.getPublicationsByTypeFromTo(typeEnum,
                from,
                size);
    }

    @POST
    @Path("search/related")
    public List<SharedBasicPostDTO> searchRelated(final PublicationElasticDTO publicationElasticDTO) {
        return publicationService.searchRelated(publicationElasticDTO);
    }

    @GET
    @Path("search")
    public List<SharedBasicPostDTO> searchPosts(@QueryParam("query") final String queries) {
        return publicationService.searchPublications(queries);
    }

    @GET
    @Path("vkpost/{postId}")
    public PublicationElasticDTO getPublicationByVkPostId(@PathParam("postId") final String postId) {
        return publicationService.getPublicationByVkPostId(postId);
    }

    @GET
    @Path("user/{user_id}/{from}/{size}")
    public List<SharedBasicPostDTO> getUserPublications(@PathParam("user_id") final String user_id,
                                                        @PathParam("from") final int from,
                                                        @PathParam("size") final int size) {
        return publicationService.getUserPublications(user_id,
                from,
                size);
    }


    @PUT
    @Path("update/pictures/{publicationId}/{thumbnailId}")
    public Response updatePublicationPictures(@PathParam("publicationId") final Long publicationId,
                                              @PathParam("thumbnailId") final String thumbnailId,
                                              final List<PublicationPicturesDTO> publicationPicturesDTOS) {
        return Response.status(200)
                .entity(publicationService.updatePublicationPictures(publicationId, thumbnailId, publicationPicturesDTOS))
                .build();

    }

}
