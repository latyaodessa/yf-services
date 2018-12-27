package yf.post.rest;

import org.joda.time.DateTime;
import yf.post.PostService;
import yf.post.dto.PostDetailsDTO;
import yf.post.dto.SharedBasicPostDTO;
import yf.post.frontend.SitemapGeneratorService;
import yf.post.frontend.SitemapUrlDto;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
@Stateless
public class PostRestImpl {

    @Inject
    private PostService postService;

    @Inject
    private SitemapGeneratorService sitemapGeneratorService;

    @GET
    @Path("{postId}")
    public PostDetailsDTO getPostDetailsDTO(@PathParam("postId") String postId) {
        return postService.getPostDetailsDTO(postId);
    }

    @GET
    @Path("/new/{index}/{type}/{from}/{size}")
    public List<SharedBasicPostDTO> getNewPostsFromTo(@PathParam("index") String index,
                                                      @PathParam("type") String type,
                                                      @PathParam("from") int from,
                                                      @PathParam("size") int size) {
        return postService.getNewPostsFromTo(index,
                type,
                from,
                size);
    }

    @GET
    @Path("/top/{index}/{type}/{from}/{size}")
    public List<SharedBasicPostDTO> getTopPostsFromTo(@PathParam("index") String index,
                                                      @PathParam("type") String type,
                                                      @PathParam("from") int from,
                                                      @PathParam("size") int size) {
        return postService.getTopPostsFromTo(index,
                type,
                from,
                size);
    }

    @GET
    @Path("search")
    public List<SharedBasicPostDTO> searchPosts(@QueryParam("query") final String queries) {
        return postService.searchPosts(queries);
    }

    @GET
    @Path("search/related")
    public List<SharedBasicPostDTO> searchRelated(@QueryParam("query") final String queries,
                                                  @QueryParam("excludeId") final String excludeId) {
        return postService.searchRelated(queries,
                excludeId);
    }

    @GET
    @Path("sitemap/{full}")
    public List<SitemapUrlDto> generateSitemap(@PathParam("full") Boolean full) {
        final Date date = new Date();

        if (full != null && full) {
            final Date lastYears = new DateTime(date).minusYears(5)
                    .toDate();
            return sitemapGeneratorService.execute(lastYears,
                    date);
        }
        final Date yersterday = new DateTime(date).minusDays(1)
                .toDate();
        return sitemapGeneratorService.execute(yersterday,
                date);
    }

}
