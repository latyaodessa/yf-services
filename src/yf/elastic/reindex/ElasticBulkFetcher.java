package yf.elastic.reindex;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vk.logic.entities.Post;

public class ElasticBulkFetcher implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private static final int DEFAULT_BULK_SIZE = 500;
	


	@PersistenceContext
	private EntityManager em;

	protected List<Post> fetchAllModels(final int offset) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);

        query.select(root).orderBy(cb.asc(root.get("id")));
        query.where(limitFetchModels(cb, root, offset));
        query.distinct(true);
        return em.createQuery(query).getResultList();
    }
	
    protected Predicate limitFetchModels(final CriteriaBuilder cb,
            final Root<Post> root,
            final int offset) {
    		Long topBorder = getIdOffset(offset + DEFAULT_BULK_SIZE - 1);
    			if (topBorder != null) {
    					return cb.between(root.get("id"), getIdOffset(offset), topBorder);
    					} else {
    						return cb.ge(root.get("id"), getIdOffset(offset));
    					}
    }
    
    private Long getIdOffset(final int offset) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Post> root = query.from(Post.class);
        query.select(root.get("id")).orderBy(cb.asc(root.get("id")));
        query.distinct(true);
        try {
            return em.createQuery(query).setFirstResult(offset).setMaxResults(1).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
    
	public static int getDefaultBulkSize() {
		return DEFAULT_BULK_SIZE;
	}

}

