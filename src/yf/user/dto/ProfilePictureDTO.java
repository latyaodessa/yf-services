package yf.user.dto;

public class ProfilePictureDTO {

    private String fileId;
    private String fileName;
    private String friendlyLink;
    private String nativeLink;
    private String sha1;

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

    public String getSha1() {
        return sha1;
    }

    public void setSha1(final String sha1) {
        this.sha1 = sha1;
    }
}
