package yf.user.dto;

import java.util.Date;

public class UserDto {
    private Long id;
    private String email;
    private UserTypeEnum type;
    private UserStatusEnum status;
    private Date createdOn;
    private String nickName;
    private String lastName;
    private String firstName;
    private String gender;
    private Boolean authorized;
    private ProfilePictureDTO profilePictureDTO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ProfilePictureDTO getProfilePictureDTO() {
        return profilePictureDTO;
    }

    public void setProfilePictureDTO(ProfilePictureDTO profilePictureDTO) {
        this.profilePictureDTO = profilePictureDTO;
    }
}
