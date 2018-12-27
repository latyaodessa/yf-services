package yf.submission.services;

import yf.post.parser.workflow.PostParserWorkflow;
import yf.publication.PublicationConverter;
import yf.publication.dtos.PublicationParticipantDTO;
import yf.publication.entities.Publication;
import yf.publication.entities.PublicationParticipant;
import yf.publication.entities.PublicationUser;
import yf.submission.dtos.PhotoshootingParticipantTypeEnum;
import yf.submission.dtos.SubmissionDTO;
import yf.submission.dtos.SubmissionParticipantDTO;
import yf.submission.dtos.SubmissionStatusEnum;
import yf.submission.entities.Submission;
import yf.submission.entities.SubmissionParticipant;
import yf.user.UserConverter;
import yf.user.UserDao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class SubmissionConverter {
    //    @Inject
//    private MetaDataDao metaDataDao;
//    @Inject
//    private CountryConverter countryConverter;
    @Inject
    private UserConverter userConverter;
    @Inject
    private UserDao userDao;
    @PersistenceContext
    private EntityManager em;
    @Inject
    private PostParserWorkflow postParserWorkflow;

//    @Inject
//    private MetaDataDao metaDataDao;

    public SubmissionParticipant participantDTOToEntity(final SubmissionParticipantDTO dto, final PhotoshootingParticipantTypeEnum typeEnum) {
        SubmissionParticipant entity = new SubmissionParticipant();
        entity.setCity(dto.getCity());
        entity.setNumber(dto.getNumber());
        entity.setCountry(dto.getCountry());
        entity.setAgency(dto.getAgency());

        entity.setCity(dto.getCity());
        entity.setCountry(dto.getCountry());
        entity.setFacebook(dto.getFacebook());
        entity.setInstagram(dto.getInstagram());
        entity.setMe(dto.getMe());
        entity.setType(typeEnum);
        entity.setCreatedOn(new Date().getTime());
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());

        return entity;
    }

    public void handleSubmissionConvertion(final SubmissionDTO dto, final Submission entity) {
        entity.setCreatedOn(new Date().getTime());
        entity.setText(dto.getText());
        entity.setCountry(dto.getCountry());
        entity.setCity(dto.getCity());
        entity.setEquipment(dto.getEquipment());
        entity.setEventDate(dto.getEventDate());
        entity.setStatus(SubmissionStatusEnum.SUBMITTED);
    }


    public SubmissionParticipantDTO participantEntityToDTO(final SubmissionParticipant entity) {
        SubmissionParticipantDTO dto = new SubmissionParticipantDTO();
        dto.setCity(entity.getCity());
        dto.setNumber(entity.getNumber());
        dto.setCountry(entity.getCountry());
        dto.setInstagram(entity.getInstagram());
        dto.setMe(entity.getMe());
        dto.setLastName(entity.getLastName());
        dto.setFirstName(entity.getFirstName());
        return dto;
    }

    public SubmissionDTO toDto(final Submission enity) {
        SubmissionDTO dto = new SubmissionDTO();
        dto.setCreatedOn(enity.getCreatedOn());
        dto.setId(enity.getId());
        dto.setUuid(enity.getUuid());
        dto.setText(enity.getText());
        dto.setCountry(enity.getCountry());
        dto.setCity(enity.getCity());
        dto.setEventDate(enity.getEventDate());
        dto.setStatus(enity.getStatus());
        dto.setComment(enity.getComment());

        dto.setUser(userConverter.toBasicUserDto(enity.getSubmitter()));

        enity.getSubmissionParticipants().forEach(prt -> {
            switch (prt.getType()) {
                case MD:
                    dto.getAllParticipants().getMds().add(participantEntityToDTO(prt));
                    break;
                case HAIR_STAILIST:
                    dto.getAllParticipants().getHairStylists().add(participantEntityToDTO(prt));
                    break;
                case MUA:
                    dto.getAllParticipants().getMuas().add(participantEntityToDTO(prt));
                    break;
                case PH:
                    dto.getAllParticipants().getPhs().add(participantEntityToDTO(prt));
                    break;
                case SET_DESIGNER:
                    dto.getAllParticipants().getSetDesigner().add(participantEntityToDTO(prt));
                    break;
                case WARDROBE_STYLIST:
                    dto.getAllParticipants().getWardrobeStylists().add(participantEntityToDTO(prt));
                    break;
                default:
                    break;
            }
        });

//        if (enity.getSubmissionPictures() != null) {
//            enity.getSubmissionPictures().forEach(this::submissionPictureEntityToDTO);
//        }

        return dto;
    }

    public void updateExistingSubmissionData(final Submission submission, final SubmissionDTO dto) {

        submission.setCreatedOn(new Date().getTime());

        if (dto.getStatus() != null) {
            submission.setStatus(dto.getStatus());
        }

        if (dto.getComment() != null) {
            submission.setComment(dto.getComment());
        }
        if (dto.getText() != null) {
            submission.setText(dto.getText());
        }

        if (dto.getCountry() != null) {
            submission.setCountry(dto.getCountry());
        }

        if (dto.getCity() != null) {
            submission.setCity(dto.getCity());
        }

        if (dto.getEventDate() != null) {
            submission.setEventDate(dto.getEventDate());
        }

        if (dto.getEquipment() != null) {
            submission.setEquipment(dto.getEquipment());
        }
    }

    public Publication submissionToPublication(final Submission submission) {
        Publication publication = new Publication();
        publication.setCreatedOn(new Date().getTime());

        final String link = postParserWorkflow.generateLinkFromSubmissionParticipants(publication.getId(), submission.getSubmissionParticipants());
        publication.setLink(link);

        publication.setEventDate(submission.getEventDate());
        publication.setCity(submission.getCity());
        publication.setCountry(submission.getCountry());
        publication.setText(submission.getText());
        publication.setHashtags(null); // TODO from submission
        publication.setEquipment(submission.getEquipment());
        publication.setLikes(0);
        publication.setExclusive(true); //TODO non exclusive

        em.persist(publication);
        em.flush();

        publication.getPublicationUsers().add(getPublicationUserFromSubmission(publication, submission));

        final List<PublicationParticipant> pubParticipants = submission.getSubmissionParticipants().stream().map(submissionParticipant -> publicationParticipantsToDTO(submissionParticipant, publication)).collect(Collectors.toList());

        publication.getPublicationParticipants().addAll(pubParticipants);

        em.merge(publication);

        return publication;
    }

    private PublicationParticipant publicationParticipantsToDTO(final SubmissionParticipant submissionParticipant, final Publication publication) {
        PublicationParticipant publicationParticipant = new PublicationParticipant();
        publicationParticipant.setCreatedOn(new Date().getTime());
        publicationParticipant.setId(submissionParticipant.getId());
        publicationParticipant.setNumber(submissionParticipant.getNumber());
        publicationParticipant.setFirstName(submissionParticipant.getFirstName());
        publicationParticipant.setLastName(submissionParticipant.getLastName());
        publicationParticipant.setCountry(submissionParticipant.getCountry());
        publicationParticipant.setCity(submissionParticipant.getCity());
        publicationParticipant.setType(submissionParticipant.getType());
        publicationParticipant.setInstagram(submissionParticipant.getInstagram());
        publicationParticipant.setVk(submissionParticipant.getVk());
        publicationParticipant.setFacebook(submissionParticipant.getFacebook());
        publicationParticipant.setWebsite(submissionParticipant.getWebsite());
        publicationParticipant.setAgency(submissionParticipant.getAgency());
        publicationParticipant.setPublication(publication);
        return publicationParticipant;
    }

    private PublicationUser getPublicationUserFromSubmission(final Publication publication, final Submission submission) {
        PublicationUser publicationUser = new PublicationUser();

        final PhotoshootingParticipantTypeEnum photoshootingParticipantTypeEnum = submission.getSubmissionParticipants().stream()
                .filter(SubmissionParticipant::getMe)
                .map(SubmissionParticipant::getType)
                .findFirst()
                .orElse(null); // TODO MULTIPLE ME

        publicationUser.setType(photoshootingParticipantTypeEnum);
        publicationUser.setUser(submission.getSubmitter());
        publicationUser.setPublication(publication);
        return publicationUser;
    }

}
