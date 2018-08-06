package yf.publication;

import yf.core.PropertiesReslover;
import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.post.parser.workflow.ParserPostConverter;
import yf.publication.dtos.PublicationElasticDTO;
import yf.publication.dtos.PublicationTypeEnum;
import yf.publication.dtos.PublicationUserDTO;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PublicationConverter {

    @Inject
    private PropertiesReslover properties;
    @Inject
    private ParserPostConverter postConverter;

    public Publication vkPostToPublication(final Post post) {
        Publication publication = new Publication();
        publication.setVkPost(post);
        return publication;
    }
//TODO
//    public PublicationElasticDTO publicationUserToDto(final PublicationUser publicationUser) {
//        PublicationElasticDTO dto = new PublicationElasticDTO();
//        dto.setId(publicationUser.getPublication().getId());
//        dto.setUser_id(publicationUser.getUser().getId());
//        dto.setDate(publicationUser.getPublication().getPhotoshootDate());
//        if(publicationUser.getPublication().getVkPost() != null) {
//
//        }
//    }
//
//    private void handleVkPostToPublication(final Post vkPost, PublicationElasticDTO dto){
//        dto.setMd(vkPost.get);
//    }


    public PublicationElasticDTO publicationToElasticDTO(final Publication publication) {
        PublicationElasticDTO dto = new PublicationElasticDTO();
        dto.setId(publication.getId());
        dto.setDate(publication.getCreatedOn());
        dto.setPhotoshootDate(publication.getPhotoshootDate());

        handlePublicationUsers(dto, publication.getPublicationUsers());
        // TODO NORMAL, NOT VK

        handleConvertionFromVKPost(dto, publication);


        return dto;
    }

    private void handleConvertionFromVKPost(final PublicationElasticDTO dto, final Publication publication) {
        final Post vkPost = publication.getVkPost();
        if (vkPost != null) {
            dto.setType(getPublicationType(vkPost.getText()));
            final PostElasticDTO postElasticDTO = postConverter.toElasticPostDto(vkPost);
            dto.setVkPost(postElasticDTO);
            dto.setThumbnail(postElasticDTO.getThumbnail());
            dto.setMdSimple(postElasticDTO.getMd());
            dto.setPhSimple(postElasticDTO.getPh());
            dto.setText(postElasticDTO.getText());
            dto.setDate(postElasticDTO.getDate());
        }
    }

    private PublicationTypeEnum getPublicationType(final String text) {
        if (text.contains(properties.get("tag.vk.native"))) {
            return PublicationTypeEnum.NATIVE;
        } else if (text.contains(properties.get("tag.vk.sets"))) {
            return PublicationTypeEnum.SETS;
        } else if (text.contains(properties.get("tag.vk.art"))) {
            return PublicationTypeEnum.ART;
        }
        return null;
    }

    private void handlePublicationUsers(final PublicationElasticDTO dto, final List<PublicationUser> publicationUsers) {

        List<PublicationUserDTO> phUsers = new ArrayList<>();
        List<PublicationUserDTO> mdUsers = new ArrayList<>();

        publicationUsers.forEach(publicationUser -> {

            if (ProfileUserTypeEnum.PH.equals(publicationUser.getType())) {
                phUsers.add(publicationUserToDTO(publicationUser));
            } else if (ProfileUserTypeEnum.MD.equals(publicationUser.getType())) {
                mdUsers.add(publicationUserToDTO(publicationUser));
            }
        });

        dto.setPhUsers(phUsers);
        dto.setMdUsers(mdUsers);
    }

    private PublicationUserDTO publicationUserToDTO(final PublicationUser publicationUser) {
        PublicationUserDTO dto = new PublicationUserDTO();
        dto.setFirstName(publicationUser.getUser().getFirstName());
        dto.setLastName(publicationUser.getUser().getLastName());
        dto.setProfileId(publicationUser.getId());
        dto.setUserId(publicationUser.getUser().getId());
        return dto;
    }
}
