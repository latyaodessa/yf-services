package yf.post;

import vk.parser.dto.elastic.PostElasticDTO;
import yf.post.dto.PostDetailsDTO;
import yf.post.dto.SharedBasicPostDTO;

public class PostConverter {
	
	public SharedBasicPostDTO toSharedBasicPostDTO(final PostElasticDTO dto){
		
		SharedBasicPostDTO sharedBasicPostDTO = new SharedBasicPostDTO();
		
		sharedBasicPostDTO.setId(dto.getId());
		sharedBasicPostDTO.setMd(dto.getMd());
		sharedBasicPostDTO.setPh(dto.getPh());
		sharedBasicPostDTO.setText(dto.getText());
		sharedBasicPostDTO.setThumbnail(dto.getThumbnail());
		
		return sharedBasicPostDTO;
		
	}
	
	public PostDetailsDTO toPostDetailsDTO(final PostElasticDTO dto){
		PostDetailsDTO postDetailsDTO = new PostDetailsDTO();
		postDetailsDTO.setId(dto.getId());
		postDetailsDTO.setMd(dto.getMd());
		postDetailsDTO.setPh(dto.getPh());
		postDetailsDTO.setText(dto.getText());
		postDetailsDTO.setThumbnail(dto.getThumbnail());
		postDetailsDTO.setLargePics(dto.getLargePics());
		
		return postDetailsDTO;
	}
	
	
	

}
