package yf.post.frontend;

import yf.post.entities.Post;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class SitemapGeneratorService {

    @PersistenceContext
    private EntityManager em;

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void execute(final Date from, final Date to) {
        Set<Long> posts = fetchPostsbyRange(from, to).stream().map(Post::getId).collect(Collectors.toSet());
    }

    public List<Post> fetchPostsbyRange(final Date from, final Date to) {
        TypedQuery<Post> query = em.createNamedQuery(Post.QUERY_POSTS_RANGE, Post.class)
                .setParameter("from", from)
                .setParameter("to", to);
        final List<Post> resultList = query.getResultList();
        return resultList;
    }
}
