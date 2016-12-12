package yf.user.entities;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="yf_user_type")
@Table(name = "user_yf")
public class User {
	@Id
	@NotNull
	@Column(name="id")
	private Long id;
	private String password;
	private String email;
	private boolean isUserAuthorized;
	@Column(name = "yf_user_type", insertable = false, updatable = false)
    private String yf_user_type;	
	
	public User() {
		super();
	}
	public User(Long id, String password, String email, boolean isUserAuthorized, String yf_user_type) {
		super();
		this.id = id;
		this.password = password;
		this.email = email;
		this.isUserAuthorized = isUserAuthorized;
		this.yf_user_type = yf_user_type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public boolean isUserAuthorized() {
		return isUserAuthorized;
	}
	public void setUserAuthorized(boolean isUserAuthorized) {
		this.isUserAuthorized = isUserAuthorized;
	}
	public String getYf_user_type() {
		return yf_user_type;
	}
	public void setYf_user_type(String yf_user_type) {
		this.yf_user_type = yf_user_type;
	}
	
	
	
}
