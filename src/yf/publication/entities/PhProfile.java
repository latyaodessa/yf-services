package yf.publication.entities;

import yf.core.entities.AbstractVersionEntity;
import yf.user.entities.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ph_profile")
@NamedQueries({
        @NamedQuery(name = PhProfile.QUERY_GET_PG_PROFILE_BY_USER_ID, query = "SELECT t FROM PhProfile t WHERE t.user.id = :user_id")
})
public class PhProfile extends AbstractVersionEntity {

    public static final String QUERY_GET_PG_PROFILE_BY_USER_ID = "EmailTemplate.getPhProfileByUserId";

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private String instagram;
    private Long vk;
    private String facebook;
    private String location;
    private String country;
    private String twitter;
    private String about;
    private String website;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<PublicationUser> publicationUsers;

    public PhProfile() {
        publicationUsers = new ArrayList<>();
        setCreatedOn(new Date());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public Long getVk() {
        return vk;
    }

    public void setVk(Long vk) {
        this.vk = vk;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PublicationUser> getPublicationUsers() {
        return publicationUsers;
    }

    public void setPublicationUsers(List<PublicationUser> publicationUsers) {
        this.publicationUsers = publicationUsers;
    }

//    public List<PublicationUser> getPublicationParticipants() {
//        return publicationParticipants;
//    }
//
//    public void setPublicationParticipants(List<PublicationUser> publicationParticipants) {
//        this.publicationParticipants = publicationParticipants;
//    }
}
