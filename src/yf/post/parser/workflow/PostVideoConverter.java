package yf.post.parser.workflow;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import yf.post.entities.PostVideo;
import yf.post.parser.dto.PostVideoDTO;

public class PostVideoConverter {
	final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public PostVideo toEntity(PostVideoDTO dto){
		PostVideo postVideo = new PostVideo();
		postVideo.setId(dto.getId());;
		postVideo.setOwner_id(dto.getOwner_id());
		postVideo.setTitle(dto.getTitle());
		postVideo.setDuration(dto.getDuration());
		postVideo.setDescription(dto.getDescription());
		postVideo.setDate(Instant.ofEpochSecond(dto.getDate())
		        .atZone(ZoneId.of("GMT-4"))
		        .format(formatter));
		postVideo.setViews(dto.getViews());
		postVideo.setComments(dto.getComments());
		postVideo.setPhoto_130(dto.getPhoto_130());
		postVideo.setPhoto_320(dto.getPhoto_320());
		postVideo.setPhoto_640(dto.getPhoto_640());
		postVideo.setAccess_key(dto.getAccess_key());
		postVideo.setCan_add(dto.getCan_add());
		return postVideo;
	}
}
