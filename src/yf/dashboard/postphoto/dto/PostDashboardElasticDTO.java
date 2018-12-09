package yf.dashboard.postphoto.dto;

import yf.post.dto.SharedBasicPostDTO;

public class PostDashboardElasticDTO {

    private Long id;
    private Long date;
    private long user_id;
    private SharedBasicPostDTO dto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public SharedBasicPostDTO getDto() {
        return dto;
    }

    public void setDto(SharedBasicPostDTO dto) {
        this.dto = dto;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
