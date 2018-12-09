package yf.submission.entities;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import yf.core.entities.AbstractDateEntity;
import yf.meta.entities.Country;
import yf.submission.dtos.PhotoshootingParticipantTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "submission_participant")
public class SubmissionParticipant extends AbstractDateEntity {

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private int number;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;
    //    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "city_id")
//    private City city;
    private String city;
    private Boolean me;

    @Enumerated(EnumType.STRING)
    private PhotoshootingParticipantTypeEnum type;

    private String instagram;
    private String vk;
    private String facebook;
    private String website;
    private String agency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getMe() {
        return me;
    }

    public void setMe(Boolean me) {
        this.me = me;
    }

    public PhotoshootingParticipantTypeEnum getType() {
        return type;
    }

    public void setType(PhotoshootingParticipantTypeEnum type) {
        this.type = type;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getVk() {
        return vk;
    }

    public void setVk(String vk) {
        this.vk = vk;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
