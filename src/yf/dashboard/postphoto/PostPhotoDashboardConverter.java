package yf.dashboard.postphoto;

import java.util.Date;

import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.dashboard.postphoto.dto.SavePostDTO;
import yf.dashboard.postphoto.entities.UserSavedPhotos;
import yf.dashboard.postphoto.entities.UserSavedPosts;

public class PostPhotoDashboardConverter {
	
	public UserSavedPosts savePostDTOtoEntity(SavePostDTO dto){
		UserSavedPosts entity = new UserSavedPosts();
	
		entity.setDate(new Date());
		entity.setPost_id(dto.getPost_id());
		entity.setPost_type(dto.getPost_type());
		entity.setUser_id(dto.getUser_id());
		
		return entity;		
	}
	
	public UserSavedPhotos savePhotoDTOtoEntity(SavePhotoDTO dto){
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
		dto.setPost_id(entity.getPost_id());
		dto.setUser_id(entity.getUser_id());
		dto.setDate(entity.getDate());
		dto.setPost_type(entity.getPost_type());
		
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
