package yf.publication.dtos;

import yf.post.dto.PostElasticDTO;

import java.util.Date;
import java.util.List;

public class PublicationElasticDTO {

    private Long id;
    private Date date;
    private Date photoshootDate;
    private PublicationTypeEnum type;
    private String mdSimple;
    private String phSimple;
    private String text;
    private String thumbnail;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PublicationTypeEnum getType() {
        return type;
    }

    public void setType(PublicationTypeEnum type) {
        this.type = type;
    }

    public Date getPhotoshootDate() {
        return photoshootDate;
    }

    public void setPhotoshootDate(Date photoshootDate) {
        this.photoshootDate = photoshootDate;
    }
}
