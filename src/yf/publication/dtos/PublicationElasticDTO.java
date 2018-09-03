package yf.publication.dtos;

import yf.post.dto.PostElasticDTO;

import java.util.List;

public class PublicationElasticDTO {

    private Long id;
    private Long date;
    private Long photoshootDate;
    private PublicationTypeEnum type;
    private String link;
    private String mdSimple;
    private String phSimple;
    private String text;
    private String thumbnail;
    private Integer likes;
    private PostElasticDTO vkPost;
    private List<PublicationPicturesDTO> publicationPictures;
    private List<PublicationUserDTO> phUsers;
    private List<PublicationUserDTO> mdUsers;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public PostElasticDTO getVkPost() {
        return vkPost;
    }

    public void setVkPost(PostElasticDTO vkPost) {
        this.vkPost = vkPost;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public PublicationTypeEnum getType() {
        return type;
    }

    public void setType(PublicationTypeEnum type) {
        this.type = type;
    }

    public Long getPhotoshootDate() {
        return photoshootDate;
    }

    public void setPhotoshootDate(Long photoshootDate) {
        this.photoshootDate = photoshootDate;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}
