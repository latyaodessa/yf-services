package yf.mail.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import yf.mail.dtos.LanguagesEnum;
import yf.mail.dtos.MailTemplateNamesEnum;

@Entity
@Table(name = "email_template")
@NamedQueries({@NamedQuery(name = EmailTemplate.QUERY_GET_TEMPLATE_BY_NAME_LANGUAGE,
                           query = "SELECT t FROM EmailTemplate t WHERE (t.name = :name AND t.language =:language)") })
public class EmailTemplate {

    public static final String QUERY_GET_TEMPLATE_BY_NAME_LANGUAGE = "EmailTemplate.getTemplateByNameAndLanguage";

    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private MailTemplateNamesEnum name;
    @Enumerated(EnumType.STRING)
    private LanguagesEnum language;
    private String sender;
    private String subject;
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;
    @Column(name = "html", columnDefinition = "TEXT")
    private String html;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailTemplateNamesEnum getName() {
        return name;
    }

    public void setName(MailTemplateNamesEnum name) {
        this.name = name;
    }

    public LanguagesEnum getLanguage() {
        return language;
    }

    public void setLanguage(LanguagesEnum language) {
        this.language = language;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
