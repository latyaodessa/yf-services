package yf.publication.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import yf.publication.ProfileUserTypeEnum;
import yf.user.entities.User;

@Entity
@Table(name = "publication_user")
@NamedQueries({@NamedQuery(name = PublicationUser.QUERY_GET_PUBLICATIONS_BY_USER, query = "SELECT t FROM PublicationUser t WHERE user.id = :user_id") })
public class PublicationUser {

    public static final String QUERY_GET_PUBLICATIONS_BY_USER = "PublicationUser.getPublicationsByUser";

    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private ProfileUserTypeEnum type;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publication_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private Publication publication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfileUserTypeEnum getType() {
        return type;
    }

    public void setType(ProfileUserTypeEnum type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }
}
