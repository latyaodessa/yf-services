package yf.user.dto.vk;

public class VKCityCountryDTO {
	long id;
	String title;
	
	public VKCityCountryDTO() {
		super();
	}
	public VKCityCountryDTO(long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}
