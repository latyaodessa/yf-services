package yf.submission.entities;

import yf.core.entities.AbstractDateEntity;
import yf.submission.dtos.SubmissionStatusEnum;
import yf.user.entities.User;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "submission")
@NamedQueries({
        @NamedQuery(name = Submission.QUERY_GET_SUBMISSION_BY_UUID_USERID, query = "SELECT t FROM Submission t WHERE t.submitter.id = :userId AND uuid = :uuid"),
        @NamedQuery(name = Submission.QUERY_GET_SUBMISSIONS_BY_USERID, query = "SELECT t FROM Submission t WHERE t.submitter.id = :userId ORDER BY t.createdOn DESC"),
        @NamedQuery(name = Submission.QUERY_GET_SUBMISSIONS_WITH_STATUS, query = "SELECT t FROM Submission t WHERE t.status IN (:status) ORDER BY t.createdOn DESC")

})
public class Submission extends AbstractDateEntity {

    public static final String QUERY_GET_SUBMISSION_BY_UUID_USERID = "Submission.getSubmissionByUUidUserId";
    public static final String QUERY_GET_SUBMISSIONS_BY_USERID = "Submission.getSubmissionsByUserId";
    public static final String QUERY_GET_SUBMISSIONS_WITH_STATUS = "Submission.getSubmissionsWithStatus";

    @Id
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    private String uuid;
    private String about;
    private String title;
    @JoinColumn(name = "country")
    private String country;
    private String city;
    @Column(name = "event_date")
    private Long eventDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SubmissionStatusEnum status;
    @Column(name = "comment")
    private String comment;
    private String equipment;

    @OneToMany(orphanRemoval = true,
            cascade = {CascadeType.MERGE,
                    CascadeType.PERSIST},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_fk")
    private Set<SubmissionParticipant> submissionParticipants;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submitter_id")
    private User submitter;

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

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public Set<SubmissionParticipant> getSubmissionParticipants() {
        return submissionParticipants;
    }

    public void setSubmissionParticipants(Set<SubmissionParticipant> submissionParticipants) {
        this.submissionParticipants = submissionParticipants;
    }

    public User getSubmitter() {
        return submitter;
    }

    public void setSubmitter(User submitter) {
        this.submitter = submitter;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

