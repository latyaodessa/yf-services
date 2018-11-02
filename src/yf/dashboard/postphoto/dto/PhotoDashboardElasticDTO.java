package yf.dashboard.postphoto.dto;

import java.util.Date;

public class PhotoDashboardElasticDTO {

    private Long id;
    private String photo_url;
    private long user_id;
    private Date date;
    private String note;
    private String ph;
    private String md;
    private long post_id;
    private String text;

    public PhotoDashboardElasticDTO(Long id,
                                    String photo_url,
                                    long user_id,
                                    Date date,
                                    String note,
                                    String ph,
                                    String md,
                                    long post_id,
                                    String text) {
        super();
        this.id = id;
        this.photo_url = photo_url;
        this.user_id = user_id;
        this.date = date;
        this.note = note;
        this.ph = ph;
        this.md = md;
        this.post_id = post_id;
        this.text = text;
    }

    public PhotoDashboardElasticDTO() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
