package yf.storage.rest;

import com.backblaze.b2.client.structures.B2UploadUrlResponse;
import yf.storage.StorageService;
import yf.storage.common.BacketsTypesEnum;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/storage")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class StorageRestImpl {

    @Inject
    private StorageService storageService;
    @GET
    @Path("upload/url/{bucketType}")
    public B2UploadUrlResponse getPostDetailsDTO(@PathParam("bucketType") BacketsTypesEnum bucketType) {
        return storageService.getUploadUrlRequest(bucketType);
    }

}
