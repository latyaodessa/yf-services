package yf.submission.services;

import yf.meta.services.CountryConverter;
import yf.meta.services.MetaDataDao;
import yf.submission.dtos.PhotoshootingParticipantTypeEnum;
import yf.submission.dtos.SubmissionDTO;
import yf.submission.dtos.SubmissionParticipantDTO;
import yf.submission.dtos.SubmissionStatusEnum;
import yf.submission.entities.Submission;
import yf.submission.entities.SubmissionParticipant;
import yf.user.UserConverter;
import yf.user.UserDao;

import javax.inject.Inject;
import java.util.Date;
import java.util.Optional;


public class SubmissionConverter {
//    @Inject
//    private MetaDataDao metaDataDao;
//    @Inject
//    private CountryConverter countryConverter;
    @Inject
    private UserConverter userConverter;
    @Inject
    private UserDao userDao;
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
        entity.setCountry(entity.getCountry());
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

}
