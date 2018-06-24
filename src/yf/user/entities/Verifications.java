package yf.user.entities;

import yf.core.entities.AbstractVersionEntity;
import yf.user.dto.VerificationTypesEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "verifications")
@NamedQueries({
        @NamedQuery(name = Verifications.QUERY_GET_VER_BY_VERFICATION, query = "SELECT t FROM Verifications t WHERE t.verification = :verification " +
                "AND verified = false AND t.createdOn > :yersterday")
})
public class Verifications extends AbstractVersionEntity {

    public static final String QUERY_GET_VER_BY_VERFICATION = "User.getVerByVerification";

    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private VerificationTypesEnum type;
    private String verification;
    private Boolean verified;
    @Column(name = "user_id")
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public VerificationTypesEnum getType() {
        return type;
    }

    public void setType(VerificationTypesEnum type) {
        this.type = type;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
