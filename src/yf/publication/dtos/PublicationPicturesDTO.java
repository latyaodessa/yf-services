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

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFriendlyLink() {
        return friendlyLink;
    }

    public void setFriendlyLink(String friendlyLink) {
        this.friendlyLink = friendlyLink;
    }

    public String getNativeLink() {
        return nativeLink;
    }

    public void setNativeLink(String nativeLink) {
        this.nativeLink = nativeLink;
    }

}
