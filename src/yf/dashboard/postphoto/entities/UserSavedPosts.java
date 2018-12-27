package yf.dashboard.postphoto.entities;

import yf.core.entities.AbstractDateEntity;
import yf.post.entities.Post;
import yf.publication.entities.Publication;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_saved_posts")
@NamedQueries({@NamedQuery(name = UserSavedPosts.QUERY_FIND_SAVED_POST_AND_BY_USER_ID,
                           query = "SELECT t FROM UserSavedPosts t JOIN t.post p where t.user_id = :user_id and p.id = :post_id"),
               @NamedQuery(name = UserSavedPosts.QUERY_FIND_SAVED_PULICATION_ID,
                           query = "SELECT t FROM UserSavedPosts t JOIN t.publication pub where t.user_id = :user_id and pub.id = :publication_id")

})
public class UserSavedPosts extends AbstractDateEntity {
    public static final String QUERY_FIND_SAVED_POST_AND_BY_USER_ID = "UserSavedPosts.findSavedPost";
    public static final String QUERY_FIND_SAVED_PULICATION_ID = "UserSavedPosts.findSavedPostByPubId";

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST })
    @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private Post post;
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST })
    @JoinColumn(name = "publication_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private Publication publication;
    @NotNull
    private long user_id;
    private String post_type;

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }
}
