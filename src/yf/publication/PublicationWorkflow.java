package yf.publication;

import yf.post.entities.Post;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class PublicationWorkflow {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private PublicationDao publicationDao;

    @Inject
    private PublicationConverter publicationConverter;

    public Publication createPublicationFromVkPost(final Post vkPost) {
        Publication publication = publicationConverter.vkPostToPublication(vkPost);
        em.persist(publication);
        return publication;
    }

    public List<PublicationUser> getPublishedByUserId(final Long userId) {
        return publicationDao.getPublishedSetsByUserId(userId);

    }

    public Publication getPublicationById(final Long publicationId) {
        return publicationDao.getPublicationById(publicationId);


    }
}
