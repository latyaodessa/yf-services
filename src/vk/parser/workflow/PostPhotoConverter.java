package vk.parser.workflow;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import vk.logic.entities.PostPhoto;
import vk.parser.dto.PostPhotoDTO;

public class PostPhotoConverter {
	final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public PostPhoto toEntity(PostPhotoDTO dto){
		PostPhoto postPhoto = new PostPhoto();
		postPhoto.setId(dto.getId());;
		postPhoto.setAlbum_id(dto.getAlbum_id());
		postPhoto.setOwner_id(dto.getOwner_id());
		postPhoto.setUser_id(dto.getUser_id());
		postPhoto.setPhoto_75(dto.getPhoto_75());	
		postPhoto.setPhoto_130(dto.getPhoto_130());
		postPhoto.setPhoto_604(dto.getPhoto_604());
		postPhoto.setPhoto_807(dto.getPhoto_807());
		postPhoto.setPhoto_1280(dto.getPhoto_1280());
		postPhoto.setPhoto_2560(dto.getPhoto_2560());
		postPhoto.setWidth(dto.getWidth());
		postPhoto.setHeight(dto.getHeight());
		postPhoto.setText(dto.getText());
		postPhoto.setDate(Instant.ofEpochSecond(dto.getDate())
		        .atZone(ZoneId.of("GMT-4"))
		        .format(formatter));
		postPhoto.setAccess_key(dto.getAccess_key());
		return postPhoto;
	}
	
	
}
