package vk.parser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostPhotoDTO{
	
	
	private Long id;
	private String album_id;
	private String owner_id;
	private Long user_id;
	private String photo_75;
	private String photo_130;
	private String photo_604;
	private String photo_807;
	private String photo_1280;
	private String photo_2560;
	private int width;
	private int height;
	private String text;
	private Long date;
	private String access_key;
	
	public PostPhotoDTO() {
		super();
	}

	public PostPhotoDTO(Long id, String album_id, String owner_id, Long user_id, String photo_75, String photo_130,
			String photo_604, String photo_807, String photo_1280, String photo_2560, int width, int height,
			String text, Long date, String access_key) {
		super();
		this.id = id;
		this.album_id = album_id;
		this.owner_id = owner_id;
		this.user_id = user_id;
		this.photo_75 = photo_75;
		this.photo_130 = photo_130;
		this.photo_604 = photo_604;
		this.photo_807 = photo_807;
		this.photo_1280 = photo_1280;
		this.photo_2560 = photo_2560;
		this.width = width;
		this.height = height;
		this.text = text;
		this.date = date;
		this.access_key = access_key;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAlbum_id() {
		return album_id;
	}
	public void setAlbum_id(String album_id) {
		this.album_id = album_id;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getPhoto_75() {
		return photo_75;
	}
	public void setPhoto_75(String photo_75) {
		this.photo_75 = photo_75;
	}
	public String getPhoto_130() {
		return photo_130;
	}
	public void setPhoto_130(String photo_130) {
		this.photo_130 = photo_130;
	}
	public String getPhoto_604() {
		return photo_604;
	}
	public void setPhoto_604(String photo_604) {
		this.photo_604 = photo_604;
	}
	public String getPhoto_807() {
		return photo_807;
	}
	public void setPhoto_807(String photo_807) {
		this.photo_807 = photo_807;
	}
	public String getPhoto_1280() {
		return photo_1280;
	}
	public void setPhoto_1280(String photo_1280) {
		this.photo_1280 = photo_1280;
	}
	public String getPhoto_2560() {
		return photo_2560;
	}
	public void setPhoto_2560(String photo_2560) {
		this.photo_2560 = photo_2560;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public String getAccess_key() {
		return access_key;
	}
	public void setAccess_key(String access_key) {
		this.access_key = access_key;
	}


}
