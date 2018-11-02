package yf.submission.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import yf.core.entities.AbstractDateEntity;
import yf.meta.entities.City;
import yf.meta.entities.Country;
import yf.submission.dtos.SubmissionStatusEnum;
import yf.user.entities.User;

@Entity
@Table(name = "submission")
public class Submission extends AbstractDateEntity {
    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private String uuid;
    private String text;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private Country country;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;
    @Column(name = "event_date")
    private Long eventDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubmissionStatusEnum status;
    @Column(name = "comment")
    private String comment;
    @OneToMany(orphanRemoval = true,
               cascade = {CascadeType.MERGE,
                          CascadeType.PERSIST },
               fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_fk")
    private List<SubmissionPicture> submissionPictures;
    @OneToMany(orphanRemoval = true,
               cascade = {CascadeType.MERGE,
                          CascadeType.PERSIST },
               fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_fk")
    private List<SubmissionParticipant> submissionParticipants;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitter_id")
    private User submitter;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(final Country country) {
        this.country = country;
    }

    public City getCity() {
        return city;
    }

    public void setCity(final City city) {
        this.city = city;
    }

    public Long getEventDate() {
        return eventDate;
    }

    public void setEventDate(final Long eventDate) {
        this.eventDate = eventDate;
    }

    public SubmissionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(final SubmissionStatusEnum status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public List<SubmissionPicture> getSubmissionPictures() {
        return submissionPictures;
    }

    public void setSubmissionPictures(final List<SubmissionPicture> submissionPictures) {
        this.submissionPictures = submissionPictures;
    }

    public List<SubmissionParticipant> getSubmissionParticipants() {
        return submissionParticipants;
    }

    public void setSubmissionParticipants(final List<SubmissionParticipant> submissionParticipants) {
        this.submissionParticipants = submissionParticipants;
    }

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(final User submitter) {
        this.submitter = submitter;
    }
}
