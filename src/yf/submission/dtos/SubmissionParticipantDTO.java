package yf.submission.dtos;

import yf.meta.dtos.CityDTO;
import yf.meta.dtos.CountryDTO;

public class SubmissionParticipantDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private CountryDTO country;
    private CityDTO city;
    private Boolean me;
    private Long userId;
    private PhotoshootingParticipantTypeEnum type;

    private String instagram;
    private String vk;
    private String facebook;
    private String website;
    private String agency;

    private Long createdOn;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(final CountryDTO country) {
        this.country = country;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(final CityDTO city) {
        this.city = city;
    }

    public Boolean getMe() {
        return me;
    }

    public void setMe(final Boolean me) {
        this.me = me;
    }

    public PhotoshootingParticipantTypeEnum getType() {
        return type;
    }

    public void setType(final PhotoshootingParticipantTypeEnum type) {
        this.type = type;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(final String instagram) {
        this.instagram = instagram;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(final String vk) {
        this.vk = vk;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(final String facebook) {
        this.facebook = facebook;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(final String website) {
        this.website = website;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(final String agency) {
        this.agency = agency;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(final Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }
}
