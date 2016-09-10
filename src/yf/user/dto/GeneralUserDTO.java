package yf.user.dto;

public class GeneralUserDTO {
	private Long id;
	private String email;
	private String type;
	private boolean isUserAuthorized;
	
	public GeneralUserDTO() {
		super();
	}


	public GeneralUserDTO(Long id, String email, String type, boolean isUserAuthorized) {
		super();
		this.id = id;
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
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
