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

public class ElasticBulkFetcher implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final int DEFAULT_BULK_SIZE = 500;

    @PersistenceContext
    private EntityManager em;

    public static int getDefaultBulkSize() {
        return DEFAULT_BULK_SIZE;
    }

    public <T> List<T> fetchAllModels(final int offset,
                                      Class<T> cls) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(cls);
        Root<T> root = query.from(cls);

        query.select(root)
                .orderBy(cb.desc(root.get("id")));
        query.where(limitFetchModels(cb,
                root,
                offset,
                cls));
        query.distinct(true);
        return em.createQuery(query)
                .getResultList();
    }

    protected <T> Predicate limitFetchModels(final CriteriaBuilder cb,
                                             final Root<T> root,
                                             final int offset,
                                             final Class<T> cls) {
        Long topBorder = getIdOffset(offset + DEFAULT_BULK_SIZE - 1,
                cls);
        if (topBorder != null) {
            return cb.between(root.get("id"),
                    getIdOffset(offset,
                            cls),
                    topBorder);
        } else {
            return cb.ge(root.get("id"),
                    getIdOffset(offset,
                            cls));
        }
    }

    private <T> Long getIdOffset(final int offset,
                                 Class<T> cls) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<T> root = query.from(cls);
        query.select(root.get("id"))
                .orderBy(cb.asc(root.get("id")));
        query.distinct(true);
        try {
            return em.createQuery(query)
                    .setFirstResult(offset)
                    .setMaxResults(1)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
