package yf.user.entities;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_fb")
@DiscriminatorValue("FB")
public class FBUser extends User {
	private String first_name;
	private String last_name;
	private String locale;
	private String location;
	private String gender;
	private String birthday;
	private String hometown;
	private String languages;
	private String website;
	private String small_pic;


	public FBUser() {
		super();
	}

	public FBUser(String first_name, String last_name, String locale, String location, String gender, String birthday,
			String hometown, String languages, String website, String small_pic) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.locale = locale;
		this.location = location;
		this.gender = gender;
		this.birthday = birthday;
		this.hometown = hometown;
		this.languages = languages;
		this.website = website;
		this.small_pic = small_pic;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
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
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getSmall_pic() {
		return small_pic;
	}
	public void setSmall_pic(String small_pic) {
		this.small_pic = small_pic;
	}

	
}
