package yf.publication;

import yf.post.entities.Post;
import yf.publication.dtos.PublicationDTO;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;

import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PublicationService {

    @Inject
    private PublicationWorkflow publicationWorkflow;

    public Publication createPublicationFromVkPost(final Post vkPost) {
        Publication publication = publicationWorkflow.createPublicationFromVkPost(vkPost);
        return publication;
    }

    public Set<PublicationDTO> getPublishedSetsFromVk(final Long userId) {

        final List<PublicationUser> publishedByUserId = publicationWorkflow.getPublishedByUserId(userId);

        if (publishedByUserId == null) {
            return null;
        }

        Set<Long> vkPostsIds = publishedByUserId.stream()
                .map(pu -> pu.getPublication())
                .filter(publication -> publication.getVkPost() != null)
                .map(publication -> publication.getVkPost().getId())
                .collect(Collectors.toSet());

        return null;
    }

}
