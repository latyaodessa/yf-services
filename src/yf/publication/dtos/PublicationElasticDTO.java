package yf.publication.dtos;

import yf.post.dto.PostElasticDTO;

import javax.persistence.Column;
import java.util.List;

public class PublicationElasticDTO {

    private Long id;
    private Long date;
    private Long eventDate;
    private PublicationTypeEnum type;
    private String link;
    private String mdSimple;
    private String phSimple;
    private String text;
    private String about;
    private String title;
    private String thumbnail;
    private Integer likes;
    private PostElasticDTO vkPost;
    private String city;
    private String country;
    private String hashtags;
    private String equipment;
    private List<PublicationPicturesDTO> publicationPictures;
    private List<PublicationUserDTO> phUsers;
    private List<PublicationUserDTO> mdUsers;
    private List<PublicationParticipantDTO> publicationParticipants;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getEventDate() {
        return eventDate;
    }

    public void setEventDate(Long eventDate) {
        this.eventDate = eventDate;
    }

    public PublicationTypeEnum getType() {
        return type;
    }

    public void setType(PublicationTypeEnum type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMdSimple() {
        return mdSimple;
    }

    public void setMdSimple(String mdSimple) {
        this.mdSimple = mdSimple;
    }

    public String getPhSimple() {
        return phSimple;
    }

    public void setPhSimple(String phSimple) {
        this.phSimple = phSimple;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public PostElasticDTO getVkPost() {
        return vkPost;
    }

    public void setVkPost(PostElasticDTO vkPost) {
        this.vkPost = vkPost;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
        this.hashtags = hashtags;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public List<PublicationPicturesDTO> getPublicationPictures() {
        return publicationPictures;
    }

    public void setPublicationPictures(List<PublicationPicturesDTO> publicationPictures) {
        this.publicationPictures = publicationPictures;
    }

    public List<PublicationUserDTO> getPhUsers() {
        return phUsers;
    }

    public void setPhUsers(List<PublicationUserDTO> phUsers) {
        this.phUsers = phUsers;
    }

    public List<PublicationUserDTO> getMdUsers() {
        return mdUsers;
    }

    public void setMdUsers(List<PublicationUserDTO> mdUsers) {
        this.mdUsers = mdUsers;
    }

    public List<PublicationParticipantDTO> getPublicationParticipants() {
        return publicationParticipants;
    }

    public void setPublicationParticipants(List<PublicationParticipantDTO> publicationParticipants) {
        this.publicationParticipants = publicationParticipants;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
