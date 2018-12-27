package yf.publication;

import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class PublicationDao {

    @PersistenceContext
    private EntityManager em;

    public Publication getPublicationById(final Long id) {
        return em.find(Publication.class,
                id);
    }

    public Publication getPublicationByVkPostId(final Long vkPostId) {
        TypedQuery<Publication> query = em.createNamedQuery(Publication.QUERY_GET_PUBLICATION_BY_VK_POST,
                Publication.class)
                .setParameter("vk_post_id",
                        vkPostId);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<PublicationUser> getPublishedSetsByUserId(final Long userId) {
        TypedQuery<PublicationUser> query = em.createNamedQuery(PublicationUser.QUERY_GET_PUBLICATIONS_BY_USER,
                PublicationUser.class)
                .setParameter("user_id",
                        userId);
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Publication getPublicationByLink(final String link) {
        TypedQuery<Publication> query = em.createNamedQuery(Publication.QUERY_GET_PUBLICATION_BY_LINK,
                Publication.class)
                .setParameter("link",
                        link);
        try {
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
