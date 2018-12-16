package yf.submission.dtos;

import yf.user.dto.UserDto;

public class SubmissionDTO {

    private Long id;
    private String uuid;
    private String text;
    private String country;
    private String city;
    private Long eventDate;
    private SubmissionStatusEnum status;
    private String comment;
    private AllParticipantsDTO allParticipants;
    private Long createdOn;
    private UserDto user;
    private String equipment;

    public SubmissionDTO() {
        allParticipants = new AllParticipantsDTO();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getEventDate() {
        return eventDate;
    }

    public void setEventDate(Long eventDate) {
        this.eventDate = eventDate;
    }

    public SubmissionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatusEnum status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AllParticipantsDTO getAllParticipants() {
        return allParticipants;
    }

    public void setAllParticipants(AllParticipantsDTO allParticipants) {
        this.allParticipants = allParticipants;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
}
