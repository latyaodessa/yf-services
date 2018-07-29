package yf.publications;

import yf.post.entities.Post;
import yf.publications.entities.Publication;

public class PublicationConverter {

    public Publication vkPostToPublication(final Post post){
        Publication publication = new Publication();
        publication.setVkPost(post);
        return publication;
    }

}
