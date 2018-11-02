package yf.publication.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "publication_pictures")
public class PublicationPictures {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "file_id")
    private String fileId;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "friendly_link")
    private String friendlyLink;
    @Column(name = "native_link")
    private String nativeLink;
    @Column(name = "publication_id")
    private Long publicationId;

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

    public String getNativeLink() {
        return nativeLink;
    }

    public void setNativeLink(final String nativeLink) {
        this.nativeLink = nativeLink;
    }

    public Long getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(final Long publicationId) {
        this.publicationId = publicationId;
    }
}
