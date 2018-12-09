package yf.submission.dtos;

public class SubmissionPictureDTO {

    private Long id;
    private String url;
    private Integer order;
    private Long createdOn;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(final Integer order) {
        this.order = order;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(final Long createdOn) {
        this.createdOn = createdOn;
    }
}
