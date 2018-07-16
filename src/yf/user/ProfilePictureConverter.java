package yf.user;

import yf.user.dto.ProfilePictureDTO;
import yf.user.entities.ProfilePicture;

import java.util.Date;

public class ProfilePictureConverter {

    public ProfilePictureDTO toDto(final ProfilePicture entity) {
        if(entity == null) {
            return null;
        }
        ProfilePictureDTO dto = new ProfilePictureDTO();
        dto.setFileId(entity.getFileId());
        dto.setFileName(entity.getFileName());
        dto.setFriendlyLink(entity.getFriendlyLink());
        dto.setNativeLink(entity.getFriendlyLink());
        return dto;
    }

    public ProfilePicture toEntity(final ProfilePictureDTO dto) {
        ProfilePicture entity = new ProfilePicture();
        entity.setCreatedOn(new Date());
        entity.setFileId(dto.getFileId());
        entity.setFileName(dto.getFileName());
        entity.setFriendlyLink(dto.getFriendlyLink());
        entity.setNativeLink(dto.getNativeLink());
        return entity;
    }

}
