package yf.post.parser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PostAudioDTO {

    private String id;
    private String owner_id;
    private String artist;
    private String title;
    private String duration;
    private long date;
    private String url;
    private int lyrics_id;
    private int album_id;
    private int genre_id;

    public PostAudioDTO() {
        super();
    }

    public PostAudioDTO(String id,
                        String owner_id,
                        String artist,
                        String title,
                        String duration,
                        long date,
                        String url,
                        int lyrics_id,
                        int album_id,
                        int genre_id) {
        super();
        this.id = id;
        this.owner_id = owner_id;
        this.artist = artist;
        this.title = title;
        this.duration = duration;
        this.date = date;
        this.url = url;
        this.lyrics_id = lyrics_id;
        this.album_id = album_id;
        this.genre_id = genre_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLyrics_id() {
        return lyrics_id;
    }

    public void setLyrics_id(int lyrics_id) {
        this.lyrics_id = lyrics_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public int getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

}
