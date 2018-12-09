package yf.submission.entities;

import yf.core.entities.AbstractDateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

//@Entity
//@Table(name = "submission_picture")
public class SubmissionPicture extends AbstractDateEntity {

//    @Id
//    @NotNull
//    @GeneratedValue
//    @Column(name = "id")
    private Long id;
    private String url;
//    @Column(name = "pic_order")
    private Integer order;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
