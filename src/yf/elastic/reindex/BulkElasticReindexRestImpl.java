package yf.elastic.reindex;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("reindex")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class BulkElasticReindexRestImpl {

    @Inject
    private ReindexWorkflow reindexWorkflow;

    @GET
    @Path("all")
    public boolean allReindex() {
        return  reindexWorkflow.reindexPublications() && reindexWorkflow.reindexDashboardPosts();
    }

//    @GET
//    @Path("vk/posts")
//    public boolean postsBulkReindex() {
//        return reindexWorkflow.reindexPosts();
//    }

    @GET
    @Path("publications")
    public boolean publicationsBulkReindex() {
        return reindexWorkflow.reindexPublications();
    }

    @GET
    @Path("dashboard")
    public boolean dashboardBulkReindex() {
        return reindexWorkflow.reindexDashboardPosts() && reindexWorkflow.reindexDashboardPhotos();
    }

//    @GET
//    @Path("countries")
//    public boolean countriesBulkReindex() {
//        return reindexWorkflow.reindexCountries();
//    }
//
//    @GET
//    @Path("cities")
//    public boolean citiesBulkReindex() {
//        return reindexWorkflow.reindexCities();
//    }
//}
