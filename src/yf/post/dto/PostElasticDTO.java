package yf.post.dto;

import java.util.List;

import yf.post.entities.Post;

public class PostElasticDTO extends Post {

    private String md;
    private String ph;
    private String md_translit;
    private String ph_translit;
    private String thumbnail;
    private List<String> largePics;

    public PostElasticDTO() {
        super();
    }

    public PostElasticDTO(String md,
                          String ph,
                          String md_translit,
                          String ph_translit,
                          String thumbnail,
                          List<String> largePics) {
        super();
        this.md = md;
        this.ph = ph;
        this.md_translit = md_translit;
        this.ph_translit = ph_translit;
        this.thumbnail = thumbnail;
        this.largePics = largePics;
    }

    public List<String> getLargePics() {
        return largePics;
    }

    public void setLargePics(List<String> largePics) {
        this.largePics = largePics;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getMd() {
        return md;
    }

    public void setMd(String md) {
        this.md = md;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getMd_translit() {
        return md_translit;
    }

    public void setMd_translit(String md_translit) {
        this.md_translit = md_translit;
    }

    public String getPh_translit() {
        return ph_translit;
    }

    public void setPh_translit(String ph_translit) {
        this.ph_translit = ph_translit;
    }

}
