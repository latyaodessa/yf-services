package yf.dashboard.postphoto.dto;

import java.util.Date;

public class PostDashboardElasticDTO {
	
	private Long id;
	private Long post_id;
	private long user_id;
	private Date date;
	private String post_type;
	
	
	public PostDashboardElasticDTO(Long id, Long post_id, long user_id, Date date, String post_type) {
		super();
		this.id = id;
		this.post_id = post_id;
		this.user_id = user_id;
		this.date = date;
		this.post_type = post_type;
	}
	
	public PostDashboardElasticDTO() {
		super();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPost_id() {
		return post_id;
	}
	public void setPost_id(Long post_id) {
		this.post_id = post_id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
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
	
	
	
	
	
}
