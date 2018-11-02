package yf.dashboard.postphoto;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.sort.SortOrder;

import yf.core.PropertiesResolover;
import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.elastic.core.NativeElasticSingleton;
import yf.post.dto.SharedBasicPostDTO;
import yf.publication.PublicationService;

public class PostPhotoDashboardService {

    @Inject
    private PostPhotoDashboardWorkflow postPhotoDashboardWorkflow;
    @Inject
    private NativeElasticSingleton nativeElastic;
    @Inject
    private PropertiesResolover properties;
    @Inject
    private PublicationService publicationService;

    public PostDashboardElasticDTO saveNewPublicationForUser(final Long publicationId,
                                                             final Long userId) {
        final PostDashboardElasticDTO savedPublication = getSavedPublication(userId,
                publicationId);
        if (savedPublication == null) {
            return postPhotoDashboardWorkflow.saveNewPublicationForUser(publicationId,
                    userId);
        }
        return null;
    }

    public PostDashboardElasticDTO getSavedPublication(final Long user_id,
                                                       final Long pubId) {
        return postPhotoDashboardWorkflow.findUserSavedPostByUserIdAndPublicationId(user_id,
                pubId);

    }

    public PhotoDashboardElasticDTO saveNewPhotoForUser(final SavePhotoDTO photoDto) {
        boolean isEmpty = postPhotoDashboardWorkflow.findUserSavedPhotosByUserIdAndPhotoUrl(photoDto.getUser_id(),
                photoDto.getPhoto_url())
                .isEmpty();
        return isEmpty ? postPhotoDashboardWorkflow.saveNewPhotoForUser(photoDto)
                       : null;
    }

    public List<PostDashboardElasticDTO> getSavedDashboardPosts(final String user_id,
                                                                final int from,
                                                                final int size) {

        SearchResponse res = nativeElastic.getClient()
                .prepareSearch(properties.get("elastic.index.dashboard.saved.post"))
                .setTypes(properties.get("elastic.type.dashboard"))
                .addSort("date",
                        SortOrder.DESC)
                .setFrom(from)
                .setSize(size)
                .setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.termQuery("user_id",
                                user_id)))
                .execute()
                .actionGet();

        final List<PostDashboardElasticDTO> savedDashboardPosts = postPhotoDashboardWorkflow.getSavedDashboardPosts(res);
        final List<String> collect = savedDashboardPosts.stream()
                .map(p -> Long.toString(p.getDto()
                        .getId()))
                .collect(Collectors.toList());
        final List<SharedBasicPostDTO> publicationsByIds = publicationService.getPublicationsByIds(collect);

        savedDashboardPosts.forEach(saved -> {
            SharedBasicPostDTO dto = publicationsByIds.stream()
                    .filter(pub -> pub.getId()
                            .equals(saved.getDto()
                                    .getId()))
                    .findFirst()
                    .orElse(null);
            if (dto != null) {
                saved.setDto(dto);
            }
        });

        return savedDashboardPosts;

    }

    public List<PhotoDashboardElasticDTO> getSavedDashboardPhotos(final String user_id,
                                                                  final int from,
                                                                  final int size) {

        SearchResponse res = nativeElastic.getClient()
                .prepareSearch(properties.get("elastic.index.dashboard.saved.photo"))
                .setTypes(properties.get("elastic.type.dashboard"))
                .addSort("date",
                        SortOrder.DESC)
                .setFrom(from)
                .setSize(size)
                .setExplain(true)
                .execute()
                .actionGet();

        return postPhotoDashboardWorkflow.getSavedDashboardPhotos(res);
    }

    public void deletePostFromUser(final Long savedPubId) {

        boolean isRemoved = postPhotoDashboardWorkflow.deletePostFromUser(savedPubId);

        if (isRemoved) {
            nativeElastic.getClient()
                    .prepareDelete(properties.get("elastic.index.dashboard.saved.post"),
                            properties.get("elastic.type.dashboard"),
                            String.valueOf(savedPubId))
                    .get();
        }
    }

    public boolean deletePhotoFromUser(final PhotoDashboardElasticDTO photoDashboardElasticDTO) {
        DeleteResponse response = nativeElastic.getClient()
                .prepareDelete(properties.get("elastic.index.dashboard.saved.photo"),
                        properties.get("elastic.type.dashboard"),
                        photoDashboardElasticDTO.getId()
                                .toString())
                .get();

        if (response.status() != RestStatus.OK) {
            return false;
        }

        boolean isRemoved = postPhotoDashboardWorkflow.deletePhotoFromUser(photoDashboardElasticDTO.getUser_id(),
                photoDashboardElasticDTO.getPhoto_url());
        return isRemoved;
    }

}
