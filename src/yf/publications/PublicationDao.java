package yf.publications;

import yf.publications.entities.Publication;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

public class PublicationDao {

    @PersistenceContext
    private EntityManager em;

    public Publication getPublicationById(final Long id) {
        return em.find(Publication.class, id);
    }

    public Publication getPublicationByVkPostId(final Long vkPostId) {
        TypedQuery<Publication> query = em.createNamedQuery(Publication.QUERY_GET_PUBLICATION_BY_VK_POST, Publication.class)
                .setParameter("vk_post_id", vkPostId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
