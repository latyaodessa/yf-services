package yf.post.frontend;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import org.codehaus.jackson.map.ObjectMapper;
import yf.core.JNDIPropertyHelper;
import yf.post.entities.Post;
import yf.post.parser.dto.PostDTO;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class SitemapGeneratorService {

    private static final String FRONTEND_URI = new JNDIPropertyHelper().lookup("yf.frontend.uri");


    @PersistenceContext
    private EntityManager em;


    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Map<Long, String> execute(final Date from, final Date end) {

        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final SimpleDateFormat formatFromDb = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Map<Long, String> dates = fetchPostsbyRange(from, end).stream()
                .collect(Collectors.toMap(Post::getId, p -> {
                    try {
                        return format.format(formatFromDb.parse(p.getDate())) + "+00:00";
                    } catch (ParseException e) {
                        return null;
                    }
                }));

        sendAddSitemapToFrontend(dates);
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


    private void sendAddSitemapToFrontend(final Map<Long, String> urls) {
//        JAXBContext jaxbContext = JAXBContext.newInstance();


        ClientResponse response = Client.create().resource(FRONTEND_URI + "sitemap")
                .type(MediaType.APPLICATION_JSON)
                .post(ClientResponse.class, urls);

        if (response == null || response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
        }
    }
}
