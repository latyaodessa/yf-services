package yf.meta.entities;

import javax.persistence.Column;

//@Entity
//@Table(name = "_countries")
public class Country {

//    @Id
//    @NotNull
//    @GeneratedValue
    @Column(name = "country_id")
    private Long id;
    @Column(name = "title_ru")
    private String titleRu;
    @Column(name = "title_ua")
    private String titleUa;
    @Column(name = "title_be")
    private String titleBe;
    @Column(name = "title_en")
    private String titleEn;
    @Column(name = "title_es")
    private String titleEs;
    @Column(name = "title_pt")
    private String titlePt;
    @Column(name = "title_de")
    private String titleDe;
    @Column(name = "title_fr")
    private String titleFr;
    @Column(name = "title_it")
    private String titleIt;
    @Column(name = "title_pl")
    private String titlePl;
    @Column(name = "title_ja")
    private String titleJa;
    @Column(name = "title_lv")
    private String titleLv;
    @Column(name = "title_cz")
    private String titleCz;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = titleRu;
    }

    public String getTitleUa() {
        return titleUa;
    }

    public void setTitleUa(String titleUa) {
        this.titleUa = titleUa;
    }

    public String getTitleBe() {
        return titleBe;
    }

    public void setTitleBe(String titleBe) {
        this.titleBe = titleBe;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getTitleEs() {
        return titleEs;
    }

    public void setTitleEs(String titleEs) {
        this.titleEs = titleEs;
    }

    public String getTitlePt() {
        return titlePt;
    }

    public void setTitlePt(String titlePt) {
        this.titlePt = titlePt;
    }

    public String getTitleDe() {
        return titleDe;
    }

    public void setTitleDe(String titleDe) {
        this.titleDe = titleDe;
    }

    public String getTitleFr() {
        return titleFr;
    }

    public void setTitleFr(String titleFr) {
        this.titleFr = titleFr;
    }

    public String getTitleIt() {
        return titleIt;
    }

    public void setTitleIt(String titleIt) {
        this.titleIt = titleIt;
    }

    public String getTitlePl() {
        return titlePl;
    }

    public void setTitlePl(String titlePl) {
        this.titlePl = titlePl;
    }

    public String getTitleJa() {
        return titleJa;
    }

    public void setTitleJa(String titleJa) {
        this.titleJa = titleJa;
    }

    public String getTitleLv() {
        return titleLv;
    }

    public void setTitleLv(String titleLv) {
        this.titleLv = titleLv;
    }

    public String getTitleCz() {
        return titleCz;
    }

    public void setTitleCz(String titleCz) {
        this.titleCz = titleCz;
    }

}
