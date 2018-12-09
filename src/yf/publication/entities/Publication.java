package yf.publication.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import yf.core.entities.AbstractDateEntity;
import yf.post.entities.Post;

@Entity
@Table(name = "publication")
@NamedQueries({@NamedQuery(name = Publication.QUERY_GET_PUBLICATION_BY_VK_POST, query = "SELECT t FROM Publication t WHERE t.vkPost.id = :vk_post_id "),
               @NamedQuery(name = Publication.QUERY_GET_PUBLICATION_BY_LINK, query = "SELECT t FROM Publication t WHERE t.link = :link "),
               @NamedQuery(name = Publication.QUERY_SETS_NATIVE_POSTS_RANGE, query = "SELECT t FROM Publication t WHERE t.createdOn between :from and :end") })
public class Publication extends AbstractDateEntity {

    public static final String QUERY_GET_PUBLICATION_BY_VK_POST = "Publication.getPublicationsByVkPost";
    public static final String QUERY_GET_PUBLICATION_BY_LINK = "Publication.getPublicationsByLink";
    public static final String QUERY_SETS_NATIVE_POSTS_RANGE = "Publication.getPostByRange";

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private String link;
    @Column(name = "photoshoot_date")
    private Date photoshootDate;
    private String location;
    private String country;
    private String title;
    private String about;
    private String hashtags;
    @Column(name = "additional_phs")
    private String additionalPhs;
    @Column(name = "additional_mds")
    private String additionalMds;
    @Column(name = "ph_techincal")
    private String phTechnical;
    private Integer likes;

    @OneToMany(fetch = FetchType.LAZY,
               cascade = {CascadeType.MERGE,
                          CascadeType.PERSIST })
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    private List<PublicationPictures> publicationPictures;

    @OneToMany(fetch = FetchType.LAZY,
               cascade = {CascadeType.MERGE,
                          CascadeType.PERSIST })
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    private List<PublicationUser> publicationUsers;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST })
    @JoinColumn(name = "vk_post_id", referencedColumnName = "id")
    private Post vkPost;

    public Publication() {
        publicationPictures = new ArrayList<>();
        publicationUsers = new ArrayList<>();
        setCreatedOn(new Date().getTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(final String link) {
        this.link = link;
    }

    public Date getPhotoshootDate() {
        return photoshootDate;
    }

    public void setPhotoshootDate(final Date photoshootDate) {
        this.photoshootDate = photoshootDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(final String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(final String about) {
        this.about = about;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(final String hashtags) {
        this.hashtags = hashtags;
    }

    public String getAdditionalPhs() {
        return additionalPhs;
    }

    public void setAdditionalPhs(String additionalPhs) {
        this.additionalPhs = additionalPhs;
    }

    public String getAdditionalMds() {
        return additionalMds;
    }

    public void setAdditionalMds(final String additionalMds) {
        this.additionalMds = additionalMds;
    }

    public String getPhTechnical() {
        return phTechnical;
    }

    public void setPhTechnical(final String phTechnical) {
        this.phTechnical = phTechnical;
    }

    public List<PublicationPictures> getPublicationPictures() {
        return publicationPictures;
    }

    public void setPublicationPictures(final List<PublicationPictures> publicationPictures) {
        this.publicationPictures = publicationPictures;
    }

    public Post getVkPost() {
        return vkPost;
    }

    public void setVkPost(final Post vkPost) {
        this.vkPost = vkPost;
    }

    public List<PublicationUser> getPublicationUsers() {
        return publicationUsers;
    }

    public void setPublicationUsers(final List<PublicationUser> publicationUsers) {
        this.publicationUsers = publicationUsers;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(final Integer likes) {
        this.likes = likes;
    }

}
