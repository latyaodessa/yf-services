package yf.dashboard.postphoto.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import yf.post.entities.Post;


@Entity
@Table(name = "user_saved_posts")
@NamedQueries({
	@NamedQuery(name = UserSavedPosts.QUERY_FIND_SAVED_POST_AND_BY_USER_ID, query = "SELECT t FROM UserSavedPosts t JOIN t.post p where t.user_id = :user_id and p.id = :post_id")
	})
public class UserSavedPosts {
	public static final String QUERY_FIND_SAVED_POST_AND_BY_USER_ID = "User.findeSavedPost";

	@Id
	@GeneratedValue
	private Long id;
	@OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
	@JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
	@NotNull	
	private Post post;
	@NotNull
	private long user_id;
	private Date date;
	private String post_type;

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
	public Post getPost() {
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
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
