package yf.post.parser.workflow;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.post.entities.PostPhoto;
import yf.post.parser.dto.PostAttachmentDTO;
import yf.post.parser.dto.PostDTO;

public class ParserPostConverter {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Inject
    private PostPhotoConverter postPhotoConverter;
    @Inject
    private PostRegexTextCleaner postRegexTextCleaner;

    public Post toEntity(PostDTO dto) {
        Post post = new Post();
        post.setId(dto.getId());
        post.setFrom_id(dto.getFrom_id()
                .replaceAll("-",
                        ""));
        post.setDate(Instant.ofEpochSecond(dto.getDate())
                .atZone(ZoneId.of("GMT-4"))
                .format(formatter));
        post.setText(dto.getText());
        post.setSigner_id(dto.getSigner_id());
        post.setLikes(dto.getLikes()
                .getCount());
        post.setReposts(dto.getReposts()
                .getCount());
        post.setCreatedOn(new Date());
        List<PostPhoto> postPhoto = new ArrayList<>();
        if (dto.getAttachments() != null) {
            for (PostAttachmentDTO attachmentDto : dto.getAttachments()) {
                if (attachmentDto.getType()
                        .equals("photo")) {
                    postPhoto.add(postPhotoConverter.toEntity(attachmentDto.getPhoto()));
                }
            }
            post.setPostPhoto(postPhoto);
        }
        return post;
    }

    public PostElasticDTO toElasticPostDto(final Post entity) {
        PostElasticDTO elasticDTO = new PostElasticDTO();
        elasticDTO.setId(entity.getId());
        elasticDTO.setFrom_id(entity.getFrom_id());
        elasticDTO.setDate(entity.getDate());
        elasticDTO.setSigner_id(entity.getSigner_id());
        elasticDTO.setLikes(entity.getLikes());
        elasticDTO.setReposts(entity.getReposts());
        elasticDTO.setPostPhoto(entity.getPostPhoto());
        if (!entity.getPostPhoto()
                .isEmpty()) {
            elasticDTO.setLargePics(findLargePics(entity.getPostPhoto()));
            elasticDTO.setThumbnail(findThumbnail(entity.getPostPhoto()
                    .get(0)));
        } else {
            elasticDTO.setThumbnail(StringUtils.EMPTY);
        }

        elasticDTO = postRegexTextCleaner.getCleanedText(entity,
                elasticDTO);

        return elasticDTO;
    }

    public String findThumbnail(final PostPhoto entity) {
        if (entity.getPhoto_604() != null)
            return entity.getPhoto_604();
        if (entity.getPhoto_807() != null)
            return entity.getPhoto_807();
        else
            return entity.getPhoto_130();

    }

    private List<String> findLargePics(final List<PostPhoto> pics) {
        List<String> largePics = new ArrayList<>();
        pics.forEach(pic -> largePics.add(getBiggestPic(pic)));
        return largePics;
    }

    private String getBiggestPic(final PostPhoto entity) {
        if (entity.getPhoto_2560() != null)
            return entity.getPhoto_2560();
        if (entity.getPhoto_1280() != null)
            return entity.getPhoto_1280();
        if (entity.getPhoto_807() != null)
            return entity.getPhoto_807();
        else
            return entity.getPhoto_604();
    }

}
