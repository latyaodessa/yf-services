package yf.publication;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import yf.post.entities.Post;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;

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
}
