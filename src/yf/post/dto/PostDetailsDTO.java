package yf.post.dto;

import java.util.List;

public class PostDetailsDTO extends SharedBasicPostDTO {

    private List<String> largePics;

    public PostDetailsDTO() {
        super();
    }

    public List<String> getLargePics() {
        return largePics;
    }

    public void setLargePics(List<String> largePics) {
        this.largePics = largePics;
    }

}
