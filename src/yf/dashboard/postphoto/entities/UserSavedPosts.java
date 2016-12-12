package yf.dashboard.postphoto.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "user_saved_posts")
@NamedQueries({
	@NamedQuery(name = UserSavedPosts.QUERY_FIND_SAVED_POST_AND_BY_USER_ID, query = "SELECT t FROM UserSavedPosts t where t.user_id = :user_id and t.post_id = :post_id")
})
public class UserSavedPosts {
	public static final String QUERY_FIND_SAVED_POST_AND_BY_USER_ID = "User.findeSavedPost";

	@Id
	@GeneratedValue
	private Long id;
	@NotNull
	private Long post_id;
	@NotNull
	private long user_id;
	private Date date;
	private String post_type;

	public UserSavedPosts() {
		super();
	}
	public UserSavedPosts(Long id, Long post_id, long user_id, Date date, String post_type) {
		super();
		this.id = id;
		this.post_id = post_id;
		this.user_id = user_id;
		this.date = date;
		this.post_type = post_type;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
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
