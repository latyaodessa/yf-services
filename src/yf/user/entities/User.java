package yf.user.entities;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnTransformer;

import yf.core.entities.AbstractVersionEntity;
import yf.user.dto.UserStatusEnum;
import yf.user.dto.UserTypeEnum;

@Entity
@Table(name = "user_yf")
@NamedQueries({@NamedQuery(name = User.QUERY_GET_USER_BY_EMAIL_OR_NICKNAME, query = "SELECT t FROM User t WHERE (t.nickName = :user) OR (t.email =:user)") })
public class User extends AbstractVersionEntity {

    public static final String QUERY_GET_USER_BY_EMAIL_OR_NICKNAME = "User.getVkUserByUserId";

    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    @ColumnTransformer(read = "pgp_sym_decrypt(" + "    password, " + "    current_setting('encrypt.key')" + ")",
                       write = "pgp_sym_encrypt( " + "    ?, " + "    current_setting('encrypt.key')" + ") ")
    @Column(columnDefinition = "bytea")
    private String password;
    private String email;
    private boolean authorize;
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserTypeEnum type;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatusEnum status;
    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "first_name")
    private String firstName;
    private String gender;
    private String phone;
    private String passport;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST })
    @JoinColumn(name = "profil_picture", referencedColumnName = "id", nullable = false)
    private ProfilePicture profilePicture;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL })
    @JoinColumn(name = "verification_id", referencedColumnName = "id", nullable = false)
    @NotNull
    private UserVerifications verifications;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public boolean isAuthorize() {
        return authorize;
    }

    public void setAuthorize(final boolean authorize) {
        this.authorize = authorize;
    }

    public UserTypeEnum getType() {
        return type;
    }

    public void setType(final UserTypeEnum type) {
        this.type = type;
    }

    public UserStatusEnum getStatus() {
        return status;
    }

    public void setStatus(final UserStatusEnum status) {
        this.status = status;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(final String nickName) {
        this.nickName = nickName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(final String passport) {
        this.passport = passport;
    }

    public UserVerifications getVerifications() {
        return verifications;
    }

    public void setVerifications(final UserVerifications verifications) {
        this.verifications = verifications;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(final String gender) {
        this.gender = gender;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(final ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }
}
