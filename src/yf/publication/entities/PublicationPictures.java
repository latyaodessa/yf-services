package yf.publication.entities;

import yf.core.entities.AbstractDateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "publication_pictures")
public class PublicationPictures extends AbstractDateEntity {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "file_id")
    private String fileId;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "friendly_link")
    private String friendlyLink;
    @Column(name = "publication_id")
    private Long publicationId;
    @Column(name = "sha1")
    private String sha1;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(final String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getFriendlyLink() {
        return friendlyLink;
    }

    public void setFriendlyLink(final String friendlyLink) {
        this.friendlyLink = friendlyLink;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(final Long publicationId) {
        this.publicationId = publicationId;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }
}
