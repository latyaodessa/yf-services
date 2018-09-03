package yf.dashboard.postphoto;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import yf.core.ElasticException;
import yf.core.PropertiesReslover;
import yf.core.elastic.ElasticToObjectConvertor;
import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.dto.PostDashboardElasticDTO;
import yf.dashboard.postphoto.dto.SavePhotoDTO;
import yf.dashboard.postphoto.dto.UserVerificationDto;
import yf.dashboard.postphoto.entities.UserSavedPhotos;
import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.post.entities.Post;
import yf.publication.PublicationDao;
import yf.publication.bulkworkflow.PublicationBulkWorkflow;
import yf.publication.entities.Publication;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PostPhotoDashboardWorkflow {

    @PersistenceContext
    private EntityManager em;
    @Inject
    private NativeElasticSingleton nativeElastic;
    @Inject
    private ElasticWorkflow elasticWorkflow;
    @Inject
    private PropertiesReslover properties;
    @Inject
    private PostPhotoDashboardConverter converter;
    @Inject
    private ElasticToObjectConvertor elasticToObjectConvertor;
    @Inject
    private PublicationDao publicationDao;
    @Inject
    private PublicationBulkWorkflow publicationBulkWorkflow;

    public PostDashboardElasticDTO saveNewPublicationForUser(final Long publicationId, final Long userId) {
        final Publication publication = publicationDao.getPublicationById(publicationId);

        UserSavedPosts entity = converter.savePostDTOtoEntity(userId, publication);
        entity.setPost(publication.getVkPost());
        entity.setPublication(publication);
        em.persist(entity);
        em.flush();


        PostDashboardElasticDTO elasticDto = converter.toPostDashboardElasticDTO(entity);

        IndexResponse response = nativeElastic.getClient()
                .prepareIndex(properties.get("elastic.index.dashboard.saved.post"),
                        properties.get("elastic.type.dashboard"),
                        entity.getId().toString())
                .setSource(elasticWorkflow.objectToSource(elasticDto))
                .get();

        if (response.status() != RestStatus.CREATED) {
            throw new ElasticException("Post Wasn't CREATED IN ELASTIC " + response.status());
        }

        updateLikes(publication, true);

        return elasticDto;
    }


    private void updateLikes(final Publication publication, final boolean increase) {
        Integer likes = publication.getLikes();
        if (likes == null) {
            likes = 0;
        }

        if (!increase) {
            likes = likes != 0 ? likes - 1 : 0;
        }

        if (increase) {
            likes += 1;
        }

        publication.setLikes(likes);
        em.merge(publication);

        publicationBulkWorkflow.execute(Collections.singletonList(publication));

    }

    private Post getPostToSave(final Long post_id) {
        TypedQuery<Post> query = em.createNamedQuery(Post.QUERY_POST_BY_ID, Post.class)
                .setParameter("post_id", post_id);
        return query.getSingleResult();
    }


    public PhotoDashboardElasticDTO saveNewPhotoForUser(final SavePhotoDTO photoDto) {

        UserSavedPhotos entity = converter.savePhotoDTOtoEntity(photoDto);
        em.persist(entity);
        em.flush();

        PhotoDashboardElasticDTO elasticDto = converter.toPhotoDashboardElasticDTO(entity);

        IndexResponse response = nativeElastic.getClient()
                .prepareIndex(properties.get("elastic.index.dashboard.saved.photo"),
                        properties.get("elastic.type.dashboard"),
                        entity.getId().toString())
                .setSource(elasticWorkflow.objectToSource(elasticDto))
                .get();

        if (response.status() == RestStatus.CREATED) {
            return elasticDto;
        }

        return null;

    }

    public List<PostDashboardElasticDTO> getSavedDashboardPosts(final SearchResponse res) {
        List<PostDashboardElasticDTO> listDto = new ArrayList<PostDashboardElasticDTO>();

        SearchHit[] hits = res.getHits().getHits();
        for (SearchHit hit : hits) {
            PostDashboardElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PostDashboardElasticDTO.class);
            listDto.add(dto);
        }
        return listDto;
    }

    public List<PhotoDashboardElasticDTO> getSavedDashboardPhotos(final SearchResponse res) {
        List<PhotoDashboardElasticDTO> listDto = new ArrayList<PhotoDashboardElasticDTO>();

        SearchHit[] hits = res.getHits().getHits();
        for (SearchHit hit : hits) {
            PhotoDashboardElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PhotoDashboardElasticDTO.class);
            listDto.add(dto);
        }
        return listDto;
    }

    public boolean deletePostFromUser(final Long savedPubId) {

        UserSavedPosts savedPubication = findSavedPublicationById(savedPubId);

        if (savedPubication == null) {
            return false;
        }

        updateLikes(savedPubication.getPublication(), false);

        em.remove(savedPubication);
        em.flush();
        return true;
    }

    public boolean deletePhotoFromUser(final Long user_id, final String photo_url) {

        List<UserSavedPhotos> userSavedPhotos = findUserSavedPhotosByUserIdAndPhotoUrl(user_id, photo_url);

        if (userSavedPhotos == null || userSavedPhotos.isEmpty() || userSavedPhotos.size() > 1) {
            return false;
        }

        for (UserSavedPhotos saved : userSavedPhotos) {
            em.remove(saved);
        }
        em.flush();
        return true;
    }

    public UserSavedPosts findSavedPublicationById(final Long savedPublicationId) {
        return em.find(UserSavedPosts.class, savedPublicationId);
    }

    public List<UserSavedPosts> findUserSavedPostByUserIdAndPostId(Long user_id, Long post_id) {
        TypedQuery<UserSavedPosts> query = em.createNamedQuery(UserSavedPosts.QUERY_FIND_SAVED_POST_AND_BY_USER_ID, UserSavedPosts.class)
                .setParameter("user_id", user_id).setParameter("post_id", post_id);

        return query.getResultList();
    }

    public PostDashboardElasticDTO findUserSavedPostByUserIdAndPublicationId(Long user_id, Long publicationId) {
        TypedQuery<UserSavedPosts> query = em.createNamedQuery(UserSavedPosts.QUERY_FIND_SAVED_PULICATION_ID, UserSavedPosts.class)
                .setParameter("user_id", user_id).setParameter("publication_id", publicationId);

        try {
            final UserSavedPosts singleResult = query.getSingleResult();
            return converter.toPostDashboardElasticDTO(singleResult);

        } catch (NoResultException e) {
            return null;
        }
    }

    public List<UserSavedPhotos> findUserSavedPhotosByUserIdAndPhotoUrl(Long user_id, String photo_url) {
        TypedQuery<UserSavedPhotos> query = em.createNamedQuery(UserSavedPhotos.QUERY_FIND_SAVED_PHOTO_AND_BY_USER_ID, UserSavedPhotos.class)
                .setParameter("user_id", user_id).setParameter("photo_url", photo_url);

        return query.getResultList();
    }
}
