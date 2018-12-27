package yf.user.entities;

import yf.core.entities.AbstractVersionEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_vk")
@NamedQueries({@NamedQuery(name = VKUser.QUERY_GET_VK_USER_BY_USER_ID, query = "SELECT t FROM VKUser t WHERE t.user.id = :user_id") })
public class VKUser extends AbstractVersionEntity {

    public static final String QUERY_GET_VK_USER_BY_USER_ID = "VKUser.getVkUserByUserId";

    @Id
    @NotNull
    @Column(name = "id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST })
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private User user;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    private String sex;
    private String bdate;
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Column(name = "home_phone")
    private String homePhone;
    private Boolean verified;
    private String country;
    private String city;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }
}
