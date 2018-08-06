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
    ReindexWorkflow reindexWorkflow;

    @GET
    @Path("all")
    public boolean allReindex() {
        return reindexWorkflow.reindexPosts()
                && reindexWorkflow.reindexDashboardPosts()
                && reindexWorkflow.reindexDashboardPhotos()
                && reindexWorkflow.reindexPublications();
    }

    @GET
    @Path("vk/posts")
    public boolean postsBulkReindex() {
        return reindexWorkflow.reindexPosts();
    }

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
}
