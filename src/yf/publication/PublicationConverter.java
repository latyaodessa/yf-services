package yf.publication;

import yf.post.entities.Post;
import yf.publication.dtos.PublicationDTO;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;

public class PublicationConverter {

    public Publication vkPostToPublication(final Post post){
        Publication publication = new Publication();
        publication.setVkPost(post);
        return publication;
    }
//TODO
//    public PublicationDTO publicationUserToDto(final PublicationUser publicationUser) {
//        PublicationDTO dto = new PublicationDTO();
//        dto.setId(publicationUser.getPublication().getId());
//        dto.setUser_id(publicationUser.getUser().getId());
//        dto.setDate(publicationUser.getPublication().getPhotoshootDate());
//        if(publicationUser.getPublication().getVkPost() != null) {
//
//        }
//    }
//
//    private void handleVkPostToPublication(final Post vkPost, PublicationDTO dto){
//        dto.setMd(vkPost.get);
//    }

}
