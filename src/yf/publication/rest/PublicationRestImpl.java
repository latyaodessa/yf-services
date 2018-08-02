package yf.publication.rest;

import yf.publication.PublicationService;
import yf.publication.dtos.PublicationDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;

@Path("publication")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class PublicationRestImpl {


    @Inject
    private PublicationService publicationService;

    @GET
    @Path("published/vk/{user_id}")
    public Set<PublicationDTO> getPublishedSetsInVk(@PathParam("user_id") final Long userId) {
        return publicationService.getPublishedSetsFromVk(userId);
    }

}
