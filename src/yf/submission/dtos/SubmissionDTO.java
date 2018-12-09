package yf.submission.dtos;

import yf.user.dto.UserDto;

import java.util.HashSet;
import java.util.Set;

public class SubmissionDTO {

    private Long id;
    private String uuid;
    private String text;
    private CountryFrontendDto country;
    private String city;
    private Long eventDate;
    private SubmissionStatusEnum status;
    private String comment;
    private Set<SubmissionPictureDTO> submissionPictures;
    private AllParticipantsDTO allParticipants;
    private Long createdOn;
    private UserDto user;
    private String equipment;

    public SubmissionDTO() {
        submissionPictures = new HashSet<>();
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

    public CountryFrontendDto getCountry() {
        return country;
    }

    public void setCountry(CountryFrontendDto country) {
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


    public Set<SubmissionPictureDTO> getSubmissionPictures() {
        return submissionPictures;
    }

    public void setSubmissionPictures(Set<SubmissionPictureDTO> submissionPictures) {
        this.submissionPictures = submissionPictures;
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
