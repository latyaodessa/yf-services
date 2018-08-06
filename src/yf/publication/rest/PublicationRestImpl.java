package yf.publication.rest;

import yf.post.dto.SharedBasicPostDTO;
import yf.publication.PublicationService;
import yf.publication.dtos.PublicationElasticDTO;
import yf.publication.dtos.PublicationTypeEnum;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    @Path("get/{type}/{from}/{size}")
    public List<SharedBasicPostDTO> getPublicationsByTypeFromTo(@PathParam("type") final PublicationTypeEnum typeEnum,
                                                                @PathParam("from") final int from,
                                                                @PathParam("size") final int size) {
        return publicationService.getPublicationsByTypeFromTo(typeEnum, from, size);
    }


}