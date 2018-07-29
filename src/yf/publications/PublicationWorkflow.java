package yf.publications;

import yf.post.entities.Post;
import yf.publications.entities.Publication;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class PublicationWorkflow {


    @PersistenceContext
    private EntityManager em;

    @Inject
    private PublicationConverter publicationConverter;

    public Publication createPublicationFromVkPost(final Post vkPost) {
        Publication publication = publicationConverter.vkPostToPublication(vkPost);
        em.persist(publication);
        return publication;
    }
}
