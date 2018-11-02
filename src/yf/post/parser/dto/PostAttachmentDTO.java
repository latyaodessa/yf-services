package yf.post.parser.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostAttachmentDTO {

    String type;
    @JsonProperty("photo")
    private PostPhotoDTO photo;
    // @JsonProperty("link")
    // private PostLinkDTO link;
    @JsonProperty("audio")
    private PostAudioDTO audio;
    @JsonProperty("video")
    private PostVideoDTO video;

    public PostAttachmentDTO() {
        super();
    }

    public PostAttachmentDTO(String type,
                             PostPhotoDTO photo,
                             PostAudioDTO audio,
                             PostVideoDTO video) {
        super();
        this.type = type;
        this.photo = photo;
        // this.link = link;
        this.audio = audio;
        this.video = video;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("photo")
    public PostPhotoDTO getPhoto() {
        return photo;
    }

    @JsonProperty("photo")
    public void setPhoto(PostPhotoDTO photo) {
        this.photo = photo;
    }

    // public PostLinkDTO getLink() {
    // return link;
    // }
    // public void setLink(PostLinkDTO link) {
    // this.link = link;
    // }
    public PostAudioDTO getAudio() {
        return audio;
    }

    public void setAudio(PostAudioDTO audio) {
        this.audio = audio;
    }

    public PostVideoDTO getVideo() {
        return video;
    }

    public void setVideo(PostVideoDTO video) {
        this.video = video;
    }

}
