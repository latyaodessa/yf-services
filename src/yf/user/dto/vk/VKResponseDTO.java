package yf.user.dto.vk;

public class VKResponseDTO {
	private Long id;
	private String first_name;
	private String last_name;
	private String sex;
	private String nickname;
	private String maiden_name;
	private String domain;
	private String bdate;
	private String photo_50;
	private String photo_100;
	private String photo_200_orig;
	private String photo_200;
	private String photo_400_orig;
	private String mobile_phone;
	private String home_phone;
	private String site;
	private String verified;
	private String followers_count;
	private String university_name;
	private String interests;
	private String music;
	private String activities;
	private String movies;
	private String books;
	private String games;
	private String about;
	private String quotes;
	private VKCityCountryDTO city;
	private VKCityCountryDTO country;
	
	public VKResponseDTO() {
		super();
	}
	public VKResponseDTO(Long id, String first_name, String last_name, String sex, String nickname, String maiden_name,
			String domain, String bdate, String photo_50, String photo_100, String photo_200_orig, String photo_200,
			String photo_400_orig, String mobile_phone, String home_phone, String site, String verified,
			String followers_count, String university_name, String interests, String music, String activities,
			String movies, String books, String games, String about, String quotes, VKCityCountryDTO city,
			VKCityCountryDTO country) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.sex = sex;
		this.nickname = nickname;
		this.maiden_name = maiden_name;
		this.domain = domain;
		this.bdate = bdate;
		this.photo_50 = photo_50;
		this.photo_100 = photo_100;
		this.photo_200_orig = photo_200_orig;
		this.photo_200 = photo_200;
		this.photo_400_orig = photo_400_orig;
		this.mobile_phone = mobile_phone;
		this.home_phone = home_phone;
		this.site = site;
		this.verified = verified;
		this.followers_count = followers_count;
		this.university_name = university_name;
		this.interests = interests;
		this.music = music;
		this.activities = activities;
		this.movies = movies;
		this.books = books;
		this.games = games;
		this.about = about;
		this.quotes = quotes;
		this.city = city;
		this.country = country;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getMaiden_name() {
		return maiden_name;
	}
	public void setMaiden_name(String maiden_name) {
		this.maiden_name = maiden_name;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getBdate() {
		return bdate;
	}
	public void setBdate(String bdate) {
		this.bdate = bdate;
	}
	public String getPhoto_50() {
		return photo_50;
	}
	public void setPhoto_50(String photo_50) {
		this.photo_50 = photo_50;
	}
	public String getPhoto_100() {
		return photo_100;
	}
	public void setPhoto_100(String photo_100) {
		this.photo_100 = photo_100;
	}
	public String getPhoto_200_orig() {
		return photo_200_orig;
	}
	public void setPhoto_200_orig(String photo_200_orig) {
		this.photo_200_orig = photo_200_orig;
	}
	public String getPhoto_200() {
		return photo_200;
	}
	public void setPhoto_200(String photo_200) {
		this.photo_200 = photo_200;
	}
	public String getPhoto_400_orig() {
		return photo_400_orig;
	}
	public void setPhoto_400_orig(String photo_400_orig) {
		this.photo_400_orig = photo_400_orig;
	}
	public String getMobile_phone() {
		return mobile_phone;
	}
	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}
	public String getHome_phone() {
		return home_phone;
	}
	public void setHome_phone(String home_phone) {
		this.home_phone = home_phone;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	public String getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(String followers_count) {
		this.followers_count = followers_count;
	}
	public String getUniversity_name() {
		return university_name;
	}
	public void setUniversity_name(String university_name) {
		this.university_name = university_name;
	}
	public String getInterests() {
		return interests;
	}
	public void setInterests(String interests) {
		this.interests = interests;
	}
	public String getMusic() {
		return music;
	}
	public void setMusic(String music) {
		this.music = music;
	}
	public String getActivities() {
		return activities;
	}
	public void setActivities(String activities) {
		this.activities = activities;
	}
	public String getMovies() {
		return movies;
	}
	public void setMovies(String movies) {
		this.movies = movies;
	}
	public String getBooks() {
		return books;
	}
	public void setBooks(String books) {
		this.books = books;
	}
	public String getGames() {
		return games;
	}
	public void setGames(String games) {
		this.games = games;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getQuotes() {
		return quotes;
	}
	public void setQuotes(String quotes) {
		this.quotes = quotes;
	}
	public VKCityCountryDTO getCity() {
		return city;
	}
	public void setCity(VKCityCountryDTO city) {
		this.city = city;
	}
	public VKCityCountryDTO getCountry() {
		return country;
	}
	public void setCountry(VKCityCountryDTO country) {
		this.country = country;
	}
	
	
}
