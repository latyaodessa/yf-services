package yf.publications;

import yf.post.entities.Post;
import yf.publications.entities.Publication;

import javax.inject.Inject;

public class PublicationService {

    @Inject
    private PublicationWorkflow publicationWorkflow;

    public Publication createPublicationFromVkPost(final Post vkPost){
        Publication publication = publicationWorkflow.createPublicationFromVkPost(vkPost);
        return publication;
    }

}
