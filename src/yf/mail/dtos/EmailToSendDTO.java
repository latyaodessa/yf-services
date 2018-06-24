package yf.mail.dtos;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EmailToSendDTO {

    Map<String, String> placeHolders;
    private String from;
    private String subject;
    private String text;
    private String html;
    private Set<String> to;

    public EmailToSendDTO() {
        placeHolders = new HashMap<>();
        to = new HashSet<>();
    }

    public Map<String, String> getPlaceHolders() {
        return placeHolders;
    }

    public void setPlaceHolders(Map<String, String> placeHolders) {
        this.placeHolders = placeHolders;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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

    public Set<String> getTo() {
        return to;
    }

    public void setTo(Set<String> to) {
        this.to = to;
    }
}
