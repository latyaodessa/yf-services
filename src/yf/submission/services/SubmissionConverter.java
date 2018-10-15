package yf.submission.services;

import yf.meta.services.MetaDataDao;
import yf.submission.dtos.SubmissionParticipantDTO;
import yf.submission.entities.SubmissionParticipant;
import yf.user.UserDao;

import javax.inject.Inject;

public class SubmissionConverter {

    @Inject
    private UserDao userDao;
    @Inject
    private MetaDataDao metaDataDao;

    public SubmissionParticipant participantDTOToEntity(final SubmissionParticipantDTO dto) {
        SubmissionParticipant entity = new SubmissionParticipant();
        entity.setId(dto.getId());
        entity.setAgency(dto.getAgency());
        entity.setCity(metaDataDao.getCityById(dto.getCity().getId()));
        entity.setCountry(metaDataDao.getCountryById(dto.getCountry().getId()));
        entity.setFacebook(dto.getFacebook());
        entity.setInstagram(dto.getInstagram());
        entity.setVk(dto.getVk());
        entity.setMe(dto.getMe());
        entity.setType(dto.getType());
        entity.setCreatedOn(dto.getCreatedOn());
        entity.setWebsite(dto.getWebsite());
        entity.setUser(userDao.getUserById(dto.getUserId()));
        entity.setLastName(dto.getLastName());
        entity.setFirstName(dto.getFirstName());
        return entity;
    }
}
