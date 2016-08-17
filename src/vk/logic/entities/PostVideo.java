package vk.logic.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class PostVideo {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO) 
	private Long video_id;
	private Long id;
	private Long owner_id;
	private String title;
	private int duration;
	private String description;
	private String date;
	private Long views;
	private int comments;
	private String photo_130;
	private String photo_320;
	private String photo_640;
	private String access_key;
	private int can_add;

	public PostVideo() {
		super();
	}
	
	public PostVideo(Long video_id, Long id, Long owner_id, String title, int duration, String description, String date, Long views,
			int comments, String photo_130, String photo_320, String photo_640, String access_key, int can_add) {
		super();
		this.video_id = video_id;
		this.id = id;
		this.owner_id = owner_id;
		this.title = title;
		this.duration = duration;
		this.description = description;
		this.date = date;
		this.views = views;
		this.comments = comments;
		this.photo_130 = photo_130;
		this.photo_320 = photo_320;
		this.photo_640 = photo_640;
		this.access_key = access_key;
		this.can_add = can_add;
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(Long owner_id) {
		this.owner_id = owner_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String string) {
		this.date = string;
	}
	public Long getViews() {
		return views;
	}
	public void setViews(Long views) {
		this.views = views;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	public String getPhoto_130() {
		return photo_130;
	}
	public void setPhoto_130(String photo_130) {
		this.photo_130 = photo_130;
	}
	public String getPhoto_320() {
		return photo_320;
	}
	public void setPhoto_320(String photo_320) {
		this.photo_320 = photo_320;
	}
	public String getPhoto_640() {
		return photo_640;
	}
	public void setPhoto_640(String photo_640) {
		this.photo_640 = photo_640;
	}
	public String getAccess_key() {
		return access_key;
	}
	public void setAccess_key(String access_key) {
		this.access_key = access_key;
	}
	public int getCan_add() {
		return can_add;
	}
	public void setCan_add(int can_add) {
		this.can_add = can_add;
	}

	public Long getVideo_id() {
		return video_id;
	}

	public void setVideo_id(Long video_id) {
		this.video_id = video_id;
	}
}
