package vk.parser.workflow;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import vk.logic.entities.PostAudio;
import vk.parser.dto.PostAudioDTO;

public class PostAudioConverter {
	final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public PostAudio toEntity(PostAudioDTO dto){
		PostAudio postAudio = new PostAudio();
		postAudio.setId(dto.getId());
		postAudio.setOwner_id(dto.getOwner_id());
		postAudio.setArtist(dto.getArtist());
		postAudio.setTitle(dto.getTitle());
		postAudio.setDuration(dto.getDuration());
		postAudio.setDate(Instant.ofEpochSecond(dto.getDate())
		        .atZone(ZoneId.of("GMT-4"))
		        .format(formatter));
		postAudio.setUrl(dto.getUrl());
		postAudio.setLyrics_id(dto.getLyrics_id());
		postAudio.setAlbum_id(dto.getAlbum_id());
		postAudio.setGenre_id(dto.getGenre_id());
		return postAudio;
	}
}
