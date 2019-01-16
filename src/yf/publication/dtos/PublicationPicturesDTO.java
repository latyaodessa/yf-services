package yf.publication.dtos;

public class PublicationPicturesDTO {

    private String fileId;
    private String fileName;
    private String friendlyLink;
    private String contentSha1;

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

    public String getContentSha1() {
        return contentSha1;
    }

    public void setContentSha1(String contentSha1) {
        this.contentSha1 = contentSha1;
    }
}
