package yf.publication.entities;

import yf.core.entities.AbstractDateEntity;
import yf.post.entities.Post;
import yf.publication.dtos.PublicationTypeEnum;
import yf.submission.entities.Submission;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "publication")
@NamedQueries({@NamedQuery(name = Publication.QUERY_GET_PUBLICATION_BY_VK_POST, query = "SELECT t FROM Publication t WHERE t.vkPost.id = :vk_post_id "),
        @NamedQuery(name = Publication.QUERY_GET_PUBLICATION_BY_LINK, query = "SELECT t FROM Publication t WHERE t.link = :link "),
        @NamedQuery(name = Publication.QUERY_SETS_NATIVE_POSTS_RANGE, query = "SELECT t FROM Publication t WHERE t.createdOn between :from and :end"),
        @NamedQuery(name = Publication.QUERY_GET_PUBLICATION_BY_SUBMISSION, query = "SELECT t FROM Publication t WHERE t.submission.id = :submissionId ")})
public class Publication extends AbstractDateEntity {

    public static final String QUERY_GET_PUBLICATION_BY_VK_POST = "Publication.getPublicationsByVkPost";
    public static final String QUERY_GET_PUBLICATION_BY_LINK = "Publication.getPublicationsByLink";
    public static final String QUERY_SETS_NATIVE_POSTS_RANGE = "Publication.getPostByRange";
    public static final String QUERY_GET_PUBLICATION_BY_SUBMISSION = "Publication.getPublicationBySubmission";

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private String link;
    @Column(name = "event_date")
    private Long eventDate;
    private String city;
    private String country;
    private String text;
    private String hashtags;
    @Column(name = "additional_phs")
    private String additionalPhs;
    @Column(name = "additional_mds")
    private String additionalMds;
    private String equipment;
    private Integer likes;
    @Enumerated(EnumType.STRING)
    private PublicationTypeEnum type;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,
                    CascadeType.PERSIST})
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    private List<PublicationPictures> publicationPictures;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE,
                    CascadeType.PERSIST})
    @JoinColumn(name = "publication_id", referencedColumnName = "id")
    private List<PublicationUser> publicationUsers;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "vk_post_id", referencedColumnName = "id")
    private Post vkPost;

    @OneToMany(orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_fk")
    private List<PublicationParticipant> publicationParticipants;

    @OneToOne(orphanRemoval = true,
            cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id")
    private Submission submission;

    public Publication() {
        publicationPictures = new ArrayList<>();
        publicationUsers = new ArrayList<>();
        publicationParticipants = new ArrayList<>();
        setCreatedOn(new Date().getTime());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Long getEventDate() {
        return eventDate;
    }

    public void setEventDate(Long eventDate) {
        this.eventDate = eventDate;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHashtags() {
        return hashtags;
    }

    public void setHashtags(String hashtags) {
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

    public void setAdditionalMds(String additionalMds) {
        this.additionalMds = additionalMds;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public List<PublicationPictures> getPublicationPictures() {
        return publicationPictures;
    }

    public void setPublicationPictures(List<PublicationPictures> publicationPictures) {
        this.publicationPictures = publicationPictures;
    }

    public List<PublicationUser> getPublicationUsers() {
        return publicationUsers;
    }

    public void setPublicationUsers(List<PublicationUser> publicationUsers) {
        this.publicationUsers = publicationUsers;
    }

    public Post getVkPost() {
        return vkPost;
    }

    public void setVkPost(Post vkPost) {
        this.vkPost = vkPost;
    }

    public List<PublicationParticipant> getPublicationParticipants() {
        return publicationParticipants;
    }

    public void setPublicationParticipants(List<PublicationParticipant> publicationParticipants) {
        this.publicationParticipants = publicationParticipants;
    }


    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public PublicationTypeEnum getType() {
        return type;
    }

    public void setType(PublicationTypeEnum type) {
        this.type = type;
    }
}
