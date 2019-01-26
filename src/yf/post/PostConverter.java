package yf.post;

import yf.post.dto.PostDetailsDTO;
import yf.post.dto.PostElasticDTO;
import yf.post.dto.SharedBasicPostDTO;
import yf.publication.dtos.PublicationElasticDTO;

public class PostConverter {

    public SharedBasicPostDTO toSharedBasicPostDTO(final PostElasticDTO dto) {

        SharedBasicPostDTO sharedBasicPostDTO = new SharedBasicPostDTO();

        sharedBasicPostDTO.setId(dto.getId());
        sharedBasicPostDTO.setMd(dto.getMd());
        sharedBasicPostDTO.setPh(dto.getPh());
        sharedBasicPostDTO.setText(dto.getText());
        sharedBasicPostDTO.setThumbnail(dto.getThumbnail());

        return sharedBasicPostDTO;

    }

    public SharedBasicPostDTO publicationToSharedBasicPostDTO(final PublicationElasticDTO dto) {

        SharedBasicPostDTO sharedBasicPostDTO = new SharedBasicPostDTO();

        sharedBasicPostDTO.setId(dto.getId());
        sharedBasicPostDTO.setMd(dto.getMdSimple());
        sharedBasicPostDTO.setPh(dto.getPhSimple());
        sharedBasicPostDTO.setText(dto.getText());
        sharedBasicPostDTO.setThumbnail(dto.getThumbnail());
        sharedBasicPostDTO.setLink(dto.getLink());
        sharedBasicPostDTO.setTitle(dto.getTitle());
        sharedBasicPostDTO.setAbout(dto.getAbout());

        if (dto.getVkPost() != null) {
            Integer likes = dto.getLikes() + dto.getVkPost()
                    .getLikes();
            sharedBasicPostDTO.setLikes(likes);
        } else {
            sharedBasicPostDTO.setLikes(dto.getLikes());
        }

        return sharedBasicPostDTO;

    }

    public PostDetailsDTO toPostDetailsDTO(final PostElasticDTO dto) {
        PostDetailsDTO postDetailsDTO = new PostDetailsDTO();
        postDetailsDTO.setId(dto.getId());
        postDetailsDTO.setMd(dto.getMd());
        postDetailsDTO.setPh(dto.getPh());
        postDetailsDTO.setText(dto.getText());
        postDetailsDTO.setThumbnail(dto.getThumbnail());
        postDetailsDTO.setLargePics(dto.getLargePics());

        return postDetailsDTO;
    }

}
