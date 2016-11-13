package yf.post.dto;

import java.util.List;

public class PostDetailsDTO extends SharedBasicPostDTO{
		
	private List <String> largePics;

	public PostDetailsDTO(Long id, String md, String ph, String text, String thumbnail, List<String> largePics) {
		super(id, md, ph, text, thumbnail);
		this.largePics = largePics;
	}

	public PostDetailsDTO() {
		super();
	}

	public List<String> getLargePics() {
		return largePics;
	}

	public void setLargePics(List<String> largePics) {
		this.largePics = largePics;
	}
	
	

}
