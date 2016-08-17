package vk.parser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class CountDTO {
	
	private int count;
	
	public CountDTO() {
		super();
	}

	public CountDTO(int count) {
		super();
		this.count = count;
	}


	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	

}
