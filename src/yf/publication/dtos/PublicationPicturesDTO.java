package yf.publication.dtos;

public class PublicationPicturesDTO {

    private Long id;
    private String fileId;
    private String fileName;
    private String friendlyLink;
    private String nativeLink;

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

}
