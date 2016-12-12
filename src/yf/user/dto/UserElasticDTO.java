package yf.user.dto;

import yf.user.entities.User;

public class UserElasticDTO extends User{
	String first_name;
	String last_name;
	String thumbnail;
	
	public UserElasticDTO() {
		super();
	}

	
	public UserElasticDTO(Long id, String password, String email, boolean isUserAuthorized,
			String yf_user_type) {
		super(id, password, email, isUserAuthorized, yf_user_type);
	}


	public UserElasticDTO(String first_name, String last_name, String thumbnail) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.thumbnail = thumbnail;
	}


	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	
}
