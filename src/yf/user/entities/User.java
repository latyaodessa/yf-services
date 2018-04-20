package yf.user.entities;

import yf.core.entities.AbstractVersionEntity;
import yf.user.dto.UserStatusEnum;
import yf.user.dto.UserTypeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_yf")
public class User extends AbstractVersionEntity {
    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    private String password;
    private String email;
    private boolean authorize;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserTypeEnum type;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatusEnum status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAuthorize() {
        return authorize;
    }

    public void setAuthorize(boolean authorize) {
        this.authorize = authorize;
    }

    public UserTypeEnum getType() {
        return type;
    }

    public void setType(UserTypeEnum type) {
        this.type = type;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(UserStatusEnum status) {
        this.status = status;
    }
}
