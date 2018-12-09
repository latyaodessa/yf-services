package yf.publication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import yf.core.PropertiesResolover;
import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.post.parser.workflow.ParserPostConverter;
import yf.publication.dtos.PublicationElasticDTO;
import yf.publication.dtos.PublicationTypeEnum;
import yf.publication.dtos.PublicationUserDTO;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationUser;

public class PublicationConverter {

    @Inject
    private PropertiesResolover properties;
    @Inject
    private ParserPostConverter postConverter;

    public Publication vkPostToPublication(final Post post) {
        Publication publication = new Publication();
        publication.setVkPost(post);
        publication.setCreatedOn(getDateFromString(post.getDate()));

        return publication;
    }

    public PublicationElasticDTO publicationToElasticDTO(final Publication publication) {
        PublicationElasticDTO dto = new PublicationElasticDTO();
        dto.setId(publication.getId());
        dto.setDate(publication.getCreatedOn());
        dto.setLink(publication.getLink());
        dto.setPhotoshootDate(Optional.ofNullable(publication.getPhotoshootDate())
                .map(java.util.Date::getTime)
                .orElse(null));
        dto.setLikes(publication.getLikes());

        handlePublicationUsers(dto,
                publication.getPublicationUsers());
        // TODO NORMAL, NOT VK

        handleConvertionFromVKPost(dto,
                publication);

        return dto;
    }

    private void handleConvertionFromVKPost(final PublicationElasticDTO dto,
                                            final Publication publication) {
        final Post vkPost = publication.getVkPost();
        if (vkPost != null) {
            dto.setType(getPublicationType(vkPost.getText()));
            final PostElasticDTO postElasticDTO = postConverter.toElasticPostDto(vkPost);
            dto.setVkPost(postElasticDTO);
            dto.setThumbnail(postElasticDTO.getThumbnail());
            dto.setMdSimple(postElasticDTO.getMd());
            dto.setPhSimple(postElasticDTO.getPh());
            dto.setText(postElasticDTO.getText());

            Long dateFromVk = getDateFromString(vkPost.getDate());

            if (dateFromVk != null) {
                dto.setDate(dateFromVk);
            }
        }
    }

    private Long getDateFromString(final String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString)
                    .getTime();
        } catch (ParseException e) {
            return null;
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

    private void handlePublicationUsers(final PublicationElasticDTO dto,
                                        final List<PublicationUser> publicationUsers) {

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
        dto.setFirstName(publicationUser.getUser()
                .getFirstName());
        dto.setLastName(publicationUser.getUser()
                .getLastName());
        dto.setProfileId(publicationUser.getId());
        dto.setUserId(publicationUser.getUser()
                .getId());
        return dto;
    }
}
