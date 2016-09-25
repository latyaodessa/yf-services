package yf.user.dto.save;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import yf.user.entities.User;

public class UserPostSaveDataDTO {
	
	private Long post_id;
	private Date date;
	private String post_type;
	private long user_id;
	
	public UserPostSaveDataDTO() {
		super();
	}
	public UserPostSaveDataDTO(Long post_id, Date date, String post_type, long user_id) {
		super();
		this.post_id = post_id;
		this.date = date;
		this.post_type = post_type;
		this.user_id = user_id;
	}
	public Long getPost_id() {
		return post_id;
	}
	public void setPost_id(Long post_id) {
		this.post_id = post_id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getPost_type() {
		return post_type;
	}
	public void setPost_type(String post_type) {
		this.post_type = post_type;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	
}
