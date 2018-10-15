package yf.submission.dtos;

import yf.meta.dtos.CityDTO;
import yf.meta.dtos.CountryDTO;
import yf.submission.entities.SubmissionParticipant;
import yf.user.entities.User;

import java.util.List;

public class SubmissionDTO {

    private Long id;
    private String uuid;
    private String text;
    private CountryDTO country;
    private CityDTO city;
    private Long eventDate;
    private SubmissionStatusEnum status;
    private String comment;
    private List<SubmissionPictureDTO> submissionPictures;
    private List<SubmissionParticipant> submissionParticipants;
    private User submitter;
    private Long createdOn;

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

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
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

    public List<SubmissionPictureDTO> getSubmissionPictures() {
        return submissionPictures;
    }

    public void setSubmissionPictures(List<SubmissionPictureDTO> submissionPictures) {
        this.submissionPictures = submissionPictures;
    }

    public List<SubmissionParticipant> getSubmissionParticipants() {
        return submissionParticipants;
    }

    public void setSubmissionParticipants(List<SubmissionParticipant> submissionParticipants) {
        this.submissionParticipants = submissionParticipants;
    }

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }
}
