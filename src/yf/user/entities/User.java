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
	private String type;
	private boolean isUserAuthorized;
	
	public User() {
		super();
	}
	


	public User(Long id, String password, String email, String type, boolean isUserAuthorized) {
		super();
		this.id = id;
		this.password = password;
		this.email = email;
		this.type = type;
		this.isUserAuthorized = isUserAuthorized;
	}



	public boolean isUserAuthorized() {
		return isUserAuthorized;
	}



	public void setUserAuthorized(boolean isUserAuthorized) {
		this.isUserAuthorized = isUserAuthorized;
	}



	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	
}
