package yf.post.parser.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostLinkDTO {
	
	private String url;
	private String title;
	private String description;
	
	public PostLinkDTO() {
		super();
	}

	public PostLinkDTO(String url, String title, String description) {
		super();
		this.url = url;
		this.title = title;
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
