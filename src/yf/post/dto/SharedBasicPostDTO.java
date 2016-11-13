package yf.post.dto;

public class SharedBasicPostDTO {
	private Long id;
	private String md;
	private String ph;
	private String text;
	private String thumbnail;
	
	public SharedBasicPostDTO(Long id, String md, String ph, String text, String thumbnail) {
		super();
		this.id = id;
		this.md = md;
		this.ph = ph;
		this.text = text;
		this.thumbnail = thumbnail;
	}
	
	public SharedBasicPostDTO() {
		super();
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMd() {
		return md;
	}
	public void setMd(String md) {
		this.md = md;
	}
	public String getPh() {
		return ph;
	}
	public void setPh(String ph) {
		this.ph = ph;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	
}
