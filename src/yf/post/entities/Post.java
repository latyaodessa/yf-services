package yf.post.entities;

import yf.core.entities.AbstractVersionEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "vk_post")
@NamedQueries({
        @NamedQuery(name = Post.QUERY_POST_BY_ID, query = "SELECT t FROM Post t WHERE t.id = :post_id"),
        @NamedQuery(name = Post.QUERY_POSTS_RANGE, query = "SELECT t FROM Post t WHERE t.date between :from and :end")
})
public class Post extends AbstractVersionEntity {

    public static final String QUERY_POST_BY_ID = "Post.getPostById";
    public static final String QUERY_POSTS_RANGE = "Post.getPostByRange";

    @Id
    @NotNull
    @Column(name = "id")
    private Long id;
    private String from_id;
    private String date;
    @Column(length = 9999)
    private String text;
    private Long signer_id;
    private int likes;
    private int reposts;

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "post_fk")
    private List<PostPhoto> postPhoto;

    public Post() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getSigner_id() {
        return signer_id;
    }

    public void setSigner_id(Long signer_id) {
        this.signer_id = signer_id;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getReposts() {
        return reposts;
    }

    public void setReposts(int reposts) {
        this.reposts = reposts;
    }

    public List<PostPhoto> getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(List<PostPhoto> postPhoto) {
        this.postPhoto = postPhoto;
    }

}