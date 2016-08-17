package vk.parser.dto;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDTO {

	private Long id;
	private String from_id;
	private String owner_id;
	private long date;
	private String post_type;
	private String text;
	private Long signer_id;
	@JsonProperty("comments") 
	private CountDTO comments;
	@JsonProperty("likes") 
	private CountDTO likes;
	@JsonProperty("reposts") 
	private CountDTO reposts;
	@JsonProperty("attachments") 
	private List<PostAttachmentDTO> attachments;

	
	public PostDTO() {
		super();
	}
	public PostDTO(Long id, String from_id, String owner_id, long date, String post_type, String text, Long signer_id,
			CountDTO comments, CountDTO likes, CountDTO reposts, List<PostAttachmentDTO> attachments) {
		super();
		this.id = id;
		this.from_id = from_id;
		this.owner_id = owner_id;
		this.date = date;
		this.post_type = post_type;
		this.text = text;
		this.signer_id = signer_id;
		this.comments = comments;
		this.likes = likes;
		this.reposts = reposts;
		this.attachments = attachments;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFrom_id() {
		return from_id;
	}
	public void setFrom_id(String from_id) {
		this.from_id = from_id;
	}
	public String getOwner_id() {
		return owner_id;
	}
	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getPost_type() {
		return post_type;
	}
	public void setPost_type(String post_type) {
		this.post_type = post_type;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Long getSigner_id() {
		return signer_id;
	}
	public void setSigner_id(Long signer_id) {
		this.signer_id = signer_id;
	}
	@JsonProperty("attachments") 
	public List<PostAttachmentDTO> getAttachments() {
		return attachments;
	}
	@JsonProperty("attachments") 
	public void setAttachments(ArrayList<PostAttachmentDTO> attachments) {
		this.attachments = attachments;
	}
	public CountDTO getComments() {
		return comments;
	}
	public void setComments(CountDTO comments) {
		this.comments = comments;
	}

	public CountDTO getLikes() {
		return likes;
	}

	public void setLikes(CountDTO likes) {
		this.likes = likes;
	}

	public CountDTO getReposts() {
		return reposts;
	}

	public void setReposts(CountDTO reposts) {
		this.reposts = reposts;
	}
}
