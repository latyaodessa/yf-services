package yf.dashboard.postphoto;

import java.util.Date;

import javax.inject.Inject;

import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.dashboard.postphoto.dto.SavedPostToDashboardDTO;
import yf.dashboard.postphoto.entities.UserSavedPhotos;
import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.post.parser.workflow.ParserPostConverter;
import yf.post.parser.workflow.PostRegexTextCleaner;

public class PostPhotoDashboardConverter {
	
	@Inject
	private ParserPostConverter parserPostConverter;
	@Inject
	PostRegexTextCleaner postRegexTextCleaner;
	
	public UserSavedPosts savePostDTOtoEntity(SavedPostToDashboardDTO dto){
		UserSavedPosts entity = new UserSavedPosts();

		entity.setDate(new Date());
		entity.setPost_type("vk");
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
		dto.setPost_id(entity.getPost().getId());
		dto.setUser_id(entity.getUser_id());
		dto.setDate(entity.getDate());
		dto.setPost_type(entity.getPost_type());
		
		dto.setThumbnail(parserPostConverter.findThumbnail(entity.getPost().getPostPhoto().get(0)));
		dto.setPh(postRegexTextCleaner.getCleanedPh(entity.getPost().getText()));
		dto.setMd(postRegexTextCleaner.getCleanedMd(entity.getPost().getText()));
		dto.setText(entity.getPost().getText());
		
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
