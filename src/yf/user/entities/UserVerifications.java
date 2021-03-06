package yf.user.entities;

import yf.core.entities.AbstractVersionEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "user_verifications")
public class UserVerifications extends AbstractVersionEntity {

    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    private Boolean email;
    private Boolean phone;
    private Boolean passport;
    private Boolean company;

    public static UserVerifications generateEmptyVerification() {
        UserVerifications verifications = new UserVerifications();
        verifications.setEmail(false);
        verifications.setPhone(false);
        verifications.setPassport(false);
        verifications.setComplany(false);
        verifications.setCreatedOn(new Date());
        return verifications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getPhone() {
        return phone;
    }

    public void setPhone(Boolean phone) {
        this.phone = phone;
    }

    public Boolean getPassport() {
        return passport;
    }

    public void setPassport(Boolean passport) {
        this.passport = passport;
    }

    public Boolean getComplany() {
        return company;
    }

    public void setComplany(Boolean company) {
        this.company = company;
    }
}


