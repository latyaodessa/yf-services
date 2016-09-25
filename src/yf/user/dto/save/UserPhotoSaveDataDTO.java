package yf.user.dto.save;

import java.util.Date;

public class UserPhotoSaveDataDTO {
	private String photo_url;
	private Date date;
	private String note;
	private long user_id;
	private String ph;
	private String md;
	private long post_id;

	public UserPhotoSaveDataDTO() {
		super();
	}
	public UserPhotoSaveDataDTO(String photo_url, Date date, String note, long user_id, String ph, String md,
			long post_id) {
		super();
		this.photo_url = photo_url;
		this.date = date;
		this.note = note;
		this.user_id = user_id;
		this.ph = ph;
		this.md = md;
		this.post_id = post_id;
	}
	public long getPost_id() {
		return post_id;
	}
	public void setPost_id(long post_id) {
		this.post_id = post_id;
	}
	public String getPh() {
		return ph;
	}
	public void setPh(String ph) {
		this.ph = ph;
	}
	public String getMd() {
		return md;
	}
	public void setMd(String md) {
		this.md = md;
	}
	public String getPhoto_url() {
		return photo_url;
	}
	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	

}
