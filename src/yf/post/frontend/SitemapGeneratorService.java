package yf.post.frontend;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import yf.core.JNDIPropertyHelper;
import yf.publication.entities.Publication;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class SitemapGeneratorService {

    private static final Logger LOGGER = Logger.getLogger(SitemapGeneratorService.class.getName());

    private static final String FRONTEND_URI = new JNDIPropertyHelper().lookup("yf.frontend.uri");
    private static final String LOC_HOST = "https://youngfolks.ru/pub/";
    private static final String CHANGE_FREQ = "daily";
    private static final Double PRIORITY = 0.8;

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<SitemapUrlDto> execute(final Date from,
                                       final Date end) {

        LOGGER.info("EXECUTING SITEMAP UPDATE");

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        List<SitemapUrlDto> dates = fetchPublicationsByTimeRange(from,
                end).stream()
                        .map(p -> {
                            SitemapUrlDto dto = new SitemapUrlDto();
                            dto.setChangefreq(CHANGE_FREQ);
                            dto.setLastmod(format.format(new Date(p.getCreatedOn())) + "+00:00");
                            dto.setLoc(LOC_HOST + p.getLink());
                            dto.setPriority(PRIORITY);
                            return dto;

                        })
                        .collect(Collectors.toList());

        sendAddSitemapToFrontend(dates);

        LOGGER.info("DONE SITEMAP UPDATE");

        return dates;

    }

    private List<Publication> fetchPublicationsByTimeRange(final Date from,
                                                           final Date end) {

        TypedQuery<Publication> query = em.createNamedQuery(Publication.QUERY_SETS_NATIVE_POSTS_RANGE,
                Publication.class)
                .setParameter("from",
                        from.getTime())
                .setParameter("end",
                        end.getTime());
        return query.getResultList();
    }

    private void sendAddSitemapToFrontend(final List<SitemapUrlDto> urls) {

        ClientResponse response = Client.create()
                .resource(FRONTEND_URI + "sitemap")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class,
                        urls);

        if (response == null || response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
    }
}
