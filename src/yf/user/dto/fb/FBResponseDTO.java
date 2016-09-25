package yf.user.dto.fb;

public class FBResponseDTO {
	private Long id;
	private String first_name;
	private String last_name;
	private String locale;
	private String location;
	private String gender;
	private String birthday;
	private String email;
	private String hometown;
	private String languages;
	private String website;
	
	public FBResponseDTO() {
		super();
	}
	public FBResponseDTO(Long id, String first_name, String last_name, String locale, String location, String gender,
			String birthday, String email, String hometown, String languages, String website, FBPictureDTO picture) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.locale = locale;
		this.location = location;
		this.gender = gender;
		this.birthday = birthday;
		this.email = email;
		this.hometown = hometown;
		this.languages = languages;
		this.website = website;
		this.picture = picture;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHometown() {
		return hometown;
	}
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}
	public String getLanguages() {
		return languages;
	}
	public void setLanguages(String languages) {
		this.languages = languages;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public FBPictureDTO getPicture() {
		return picture;
	}
	public void setPicture(FBPictureDTO picture) {
		this.picture = picture;
	}
	private FBPictureDTO picture;
		
}
