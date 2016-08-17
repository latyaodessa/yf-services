package vk.parser.workflow;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import vk.logic.entities.Post;
import vk.logic.entities.PostAudio;
import vk.logic.entities.PostPhoto;
import vk.logic.entities.PostVideo;
import vk.parser.dto.PostAttachmentDTO;
import vk.parser.dto.PostDTO;

class PostConverter {
@Inject
PostPhotoConverter postPhotoConverter;
@Inject
PostAudioConverter postAudioConverter;
@Inject
PostVideoConverter postVideoConverter;
final DateTimeFormatter formatter = 
DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	public Post toEntity(PostDTO dto){
		Post post = new Post();
		post.setId(dto.getId());
		post.setFrom_id(dto.getFrom_id());
		post.setDate(Instant.ofEpochSecond(dto.getDate())
        .atZone(ZoneId.of("GMT-4"))
        .format(formatter));
		post.setText(dto.getText());
		post.setSigner_id(dto.getSigner_id());
		post.setLikes(dto.getLikes().getCount());
		post.setReposts(dto.getReposts().getCount());
		List<PostPhoto> postPhoto = new ArrayList<PostPhoto>();
		List<PostAudio> postAudio = new ArrayList<PostAudio>();
		List<PostVideo> postVideo = new ArrayList<PostVideo>();
		if(dto.getAttachments()!= null){
		for(PostAttachmentDTO attachmentDto : dto.getAttachments()){
			if(attachmentDto.getType().equals("photo")){
				postPhoto.add(postPhotoConverter.toEntity(attachmentDto.getPhoto()));
			}
			if(attachmentDto.getType().equals("video")){
				postVideo.add(postVideoConverter.toEntity(attachmentDto.getVideo()));
			}
			if(attachmentDto.getType().equals("audio")){
				postAudio.add(postAudioConverter.toEntity(attachmentDto.getAudio()));
			}
		}
		post.setPostPhoto(postPhoto);
		post.setPostAudio(postAudio);
		post.setPostVideo(postVideo);
		}
		return post;
	}

}
