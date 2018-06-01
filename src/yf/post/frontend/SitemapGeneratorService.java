package yf.post.frontend;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import yf.core.JNDIPropertyHelper;
import yf.elastic.reindex.bulkworkflow.PostBulkWorkflow;
import yf.post.entities.Post;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.MediaType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class SitemapGeneratorService {

    private static final Logger LOGGER = Logger.getLogger(SitemapGeneratorService.class.getName());


    private static final String FRONTEND_URI = new JNDIPropertyHelper().lookup("yf.frontend.uri");
    private static final String LOC_HOST = "https://youngfolks.ru/post/";
    private static final String CHANGE_FREQ = "weekly";
    private static final Double PRIORITY = 0.8;

    @PersistenceContext
    private EntityManager em;


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<SitemapUrlDto> execute(final Date from, final Date end) {

        LOGGER.info("EXECUTING SITEMAP UPDATE");

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final SimpleDateFormat formatFromDb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<SitemapUrlDto> dates = fetchPostsbyRange(from, end).stream()
                .map(p -> {
                    try {
                        SitemapUrlDto dto = new SitemapUrlDto();
                        dto.setChangefreq(CHANGE_FREQ);
                        dto.setLastmod(format.format(formatFromDb.parse(p.getDate())) + "+00:00");
                        dto.setLoc(LOC_HOST + p.getId());
                        dto.setPriority(PRIORITY);
                        return dto;
                    } catch (ParseException e) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());


        sendAddSitemapToFrontend(dates);

        LOGGER.info("DONE SITEMAP UPDATE");

        return dates;


    }

    private List<Post> fetchPostsbyRange(final Date from, final Date end) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        final String frmDate = format.format(from);
        final String enDate = format.format(end);


        TypedQuery<Post> query = em.createNamedQuery(Post.QUERY_POSTS_RANGE, Post.class)
                .setParameter("from", frmDate)
                .setParameter("end", enDate);
        final List<Post> resultList = query.getResultList();
        return resultList;
    }


    private void sendAddSitemapToFrontend(final List<SitemapUrlDto> urls) {

        ClientResponse response = Client.create().resource(FRONTEND_URI + "sitemap")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, urls);

        if (response == null || response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
    }
}
