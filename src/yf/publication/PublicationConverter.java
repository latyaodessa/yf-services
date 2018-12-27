package yf.publication;

import yf.core.PropertiesResolover;
import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.post.parser.workflow.ParserPostConverter;
import yf.publication.dtos.PublicationElasticDTO;
import yf.publication.dtos.PublicationParticipantDTO;
import yf.publication.dtos.PublicationPicturesDTO;
import yf.publication.dtos.PublicationTypeEnum;
import yf.publication.dtos.PublicationUserDTO;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationParticipant;
import yf.publication.entities.PublicationPictures;
import yf.publication.entities.PublicationUser;
import yf.submission.dtos.PhotoshootingParticipantTypeEnum;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PublicationConverter {

    @Inject
    private PropertiesResolover properties;
    @Inject
    private ParserPostConverter postConverter;

    @PersistenceContext
    private EntityManager em;

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
        dto.setEventDate(Optional.ofNullable(publication.getEventDate())
                .orElse(null));
        dto.setLikes(publication.getLikes());
        dto.setCity(publication.getCity());
        dto.setCountry(publication.getCountry());
        dto.setHashtags(publication.getHashtags());
        dto.setEquipment(publication.getEquipment());
        handlePublicationUsers(dto,
                publication.getPublicationUsers());

        if (publication.getVkPost() != null) {
            handleConvertionFromVKPost(dto,
                    publication);
        }

        if (publication.getPublicationParticipants() != null) {
            final List<PublicationParticipantDTO> collect = publication.getPublicationParticipants().stream().map(this::publicationParticipantsToDTO).collect(Collectors.toList());
            dto.setPublicationParticipants(collect);

        }

        return dto;
    }

    public PublicationPictures convertPublicationPicturesDTOToEntity(final Publication publication, final PublicationPicturesDTO dto) {
        PublicationPictures publicationPictures = new PublicationPictures();
        publicationPictures.setFileId(dto.getFileId());
        publicationPictures.setFileName(dto.getFileName());
        publicationPictures.setFriendlyLink(dto.getFriendlyLink());
        publicationPictures.setNativeLink(dto.getNativeLink());
        publicationPictures.setPublicationId(publication.getId());
        em.persist(publicationPictures);
        return publicationPictures;

    }

    private PublicationParticipantDTO publicationParticipantsToDTO(final PublicationParticipant publicationParticipant) {

        PublicationParticipantDTO dto = new PublicationParticipantDTO();
        dto.setId(publicationParticipant.getId());
        dto.setNumber(publicationParticipant.getNumber());
        dto.setFirstName(publicationParticipant.getFirstName());
        dto.setLastName(publicationParticipant.getLastName());
        dto.setCountry(publicationParticipant.getCountry());
        dto.setCity(publicationParticipant.getCity());
        dto.setType(publicationParticipant.getType());
        dto.setInstagram(publicationParticipant.getInstagram());
        dto.setVk(publicationParticipant.getVk());
        dto.setFacebook(publicationParticipant.getFacebook());
        dto.setWebsite(publicationParticipant.getWebsite());
        dto.setAgency(publicationParticipant.getAgency());
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

            if (PhotoshootingParticipantTypeEnum.PH.equals(publicationUser.getType())) {
                phUsers.add(publicationUserToDTO(publicationUser));
            } else if (PhotoshootingParticipantTypeEnum.MD.equals(publicationUser.getType())) {
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
