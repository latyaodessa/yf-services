package yf.user.dto.external;

import yf.user.dto.UserDto;

import java.util.Date;

public class VKUserDTO {
    private Long id;
    private UserDto userDto;
    private String firstName;
    private String lastName;
    private String sex;
    private String bdate;
    private String mobilePhone;
    private String homePhone;
    private Boolean verified;
    private String city;
    private String country;
    private Date createdOn;

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

    public String getSex() {
        return sex;
    }

    public void setSex(final String sex) {
        this.sex = sex;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(final String bdate) {
        this.bdate = bdate;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(final String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(final String homePhone) {
        this.homePhone = homePhone;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(final Boolean verified) {
        this.verified = verified;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(final UserDto userDto) {
        this.userDto = userDto;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(final Date createdOn) {
        this.createdOn = createdOn;
    }
}
