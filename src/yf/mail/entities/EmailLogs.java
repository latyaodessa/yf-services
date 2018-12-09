package yf.mail.entities;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.map.ObjectMapper;

import yf.core.entities.AbstractVersionEntity;

@Entity
@Table(name = "email_logs")
public class EmailLogs extends AbstractVersionEntity {

    private static final Logger LOG = Logger.getLogger(EmailLogs.class.getName());

    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue
    private Long id;
    private String content;

    public EmailLogs(final Object dto) {
        setCreatedOn(new Date());

        try {
            String jsonInString = new ObjectMapper().writerWithDefaultPrettyPrinter()
                    .writeValueAsString(dto);
            setContent(jsonInString);
        } catch (IOException e) {
            LOG.severe("Could not parse content dto");
        }
    }

    public EmailLogs() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
