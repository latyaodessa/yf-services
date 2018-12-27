package yf.dashboard.postphoto;

import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.dashboard.postphoto.entities.UserSavedPhotos;
import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.post.dto.SharedBasicPostDTO;
import yf.publication.entities.Publication;

import java.util.Date;

public class PostPhotoDashboardConverter {

    public UserSavedPosts savePostDTOtoEntity(final Long userId,
                                              final Publication publication) {
        UserSavedPosts entity = new UserSavedPosts();

        entity.setCreatedOn(new Date().getTime());
        if (publication.getVkPost() != null) {
            entity.setPost_type("vk");
        }
        entity.setUser_id(userId);

        return entity;
    }

    public UserSavedPhotos savePhotoDTOtoEntity(SavePhotoDTO dto) {
        UserSavedPhotos entity = new UserSavedPhotos();

        entity.setDate(new Date());
        entity.setPhoto_url(dto.getPhoto_url());
        entity.setNote(dto.getNote());
        entity.setUser_id(dto.getUser_id());

        entity.setPost_id(dto.getPost_id());
        entity.setMd(dto.getMd());
        entity.setPh(dto.getPh());
        entity.setText(dto.getText());

        return entity;
    }

    public PostDashboardElasticDTO toPostDashboardElasticDTO(final UserSavedPosts entity) {
        PostDashboardElasticDTO dto = new PostDashboardElasticDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getCreatedOn());
        dto.setUser_id(entity.getUser_id());

        Publication publication = entity.getPublication();
        Long publicationId = publication != null ? publication.getId()
                                                 : null;
        SharedBasicPostDTO sharedBasicPostDTO = new SharedBasicPostDTO();
        sharedBasicPostDTO.setId(publicationId);
        dto.setDto(sharedBasicPostDTO);

        return dto;
    }

    public PhotoDashboardElasticDTO toPhotoDashboardElasticDTO(final UserSavedPhotos entity) {
        PhotoDashboardElasticDTO dto = new PhotoDashboardElasticDTO();

        dto.setId(entity.getId());
        dto.setPhoto_url(entity.getPhoto_url());
        dto.setPost_id(entity.getPost_id());
        dto.setUser_id(entity.getUser_id());
        dto.setDate(entity.getDate());
        dto.setNote(entity.getNote());
        dto.setPh(entity.getPh());
        dto.setMd(entity.getMd());
        dto.setText(entity.getText());

        return dto;
    }
}
