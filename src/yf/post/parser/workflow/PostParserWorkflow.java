package yf.post.parser.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import yf.core.PropertiesReslover;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;
import yf.elastic.reindex.bulkworkflow.PostBulkWorkflow;
import yf.post.PostService;
import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.post.parser.dto.PostDTO;
import yf.publication.PublicationDao;
import yf.publication.PublicationService;
import yf.publication.entities.MdProfile;
import yf.publication.entities.PhProfile;
import yf.publication.entities.Publication;
import yf.user.UserProfileService;
import yf.user.rest.VkRestClient;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class PostParserWorkflow {

    @Inject
    private NativeElasticSingleton nativeElasticClient;
    @PersistenceContext
    private EntityManager em;
    @Inject
    private ParserPostConverter postConverter;
    @Inject
    private PostService postService;
    @Inject
    private PropertiesReslover properties;
    @Inject
    private PostBulkWorkflow postBulkWorkflow;
    @Inject
    private ElasticWorkflow elasticWorkflow;
    @Inject
    private PostRegexTextCleaner postRegexTextCleaner;
    @Inject
    private VkRestClient vkRestClient;
    @Inject
    private UserProfileService userProfileService;
    @Inject
    private PublicationService publicationService;
    @Inject
    private PublicationDao publicationDao;

    private static final Logger LOGGER = Logger.getLogger(PostBulkWorkflow.class.getName());


    public List<PostDTO> saveNewPostData(List<PostDTO> listPostDTO) {
        BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
        bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);

        persistPosts(listPostDTO, bulkRequest);

        em.flush();
        em.clear();


        return listPostDTO;
    }


    public void saveUpdateNewEntry(final List<PostDTO> postDTO) {
        BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
        bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);

        persistPosts(postDTO, bulkRequest);

    }

    private void bulkPersistToElastic(final BulkRequestBuilder bulkRequest) {
        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            LOGGER.severe("BULK ERROR");
        }
    }

    private void persistPosts(final List<PostDTO> postDTO, final BulkRequestBuilder bulkRequest) {
        postDTO.stream()
                .filter(dto -> postBulkWorkflow.getIndex(dto.getText(), postBulkWorkflow.TAG_INDEX) != null)
                .map(dto -> postConverter.toEntity(dto))
                .forEach(post -> {

                    em.merge(post);
                    PostElasticDTO postElasticDTO = postConverter.toElasticPostDto(post);

                    try {
                        postBulkWorkflow.addEntityToBulk(postElasticDTO, bulkRequest, BulkOptions.DELETE);
                        postBulkWorkflow.addEntityToBulk(postElasticDTO, bulkRequest, BulkOptions.INDEX);
                    } catch (JsonProcessingException e) {
                        LOGGER.severe("PARSE ERROR: " + e);
                    }


                });
        bulkPersistToElastic(bulkRequest);

    }

    public void saveUpdateWeeklyTop() {

        Set<String> indecies = new HashSet<String>(Arrays.asList(properties.get("elastic.index.native.top"), properties.get("elastic.index.sets.top")));
        elasticWorkflow.deleteIndicies(indecies);

        List<PostElasticDTO> nativePosts = postService.getElasticTopPostsFromTo(properties.get("elastic.index.native"),
                properties.get("elastic.type.photo"), 0, 10);
        List<PostElasticDTO> setsPosts = postService.getElasticTopPostsFromTo(properties.get("elastic.index.sets"),
                properties.get("elastic.type.photo"), 0, 10);

        BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();

        nativePosts.forEach(dto -> {
            try {
                postBulkWorkflow.addEntityToBulkWithCustomIndex(dto, bulkRequest, BulkOptions.INDEX, properties.get("elastic.index.native.top"));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        setsPosts.forEach(dto -> {
            try {
                postBulkWorkflow.addEntityToBulkWithCustomIndex(dto, bulkRequest, BulkOptions.INDEX, properties.get("elastic.index.sets.top"));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            LOGGER.log(java.util.logging.Level.WARNING, "BULK ERROR");
        } else {
            LOGGER.log(java.util.logging.Level.INFO, "WEEKLY TOP EXECUTED. Time: " + bulkResponse.getTook()
                    + " number of actions: " + bulkRequest.numberOfActions());
        }

    }

    public void addVkPostAndParticipantsToPublication(final Long postId) {

        Publication existingPublication = publicationDao.getPublicationByVkPostId(postId);

        if (existingPublication != null) {
            return;
        }

        TypedQuery<Post> query = em.createNamedQuery(Post.QUERY_POST_BY_ID, Post.class)
                .setParameter("post_id", postId);

        if (query == null) {
            return;
        }
        Post post = query.getSingleResult();

        Set<PhProfile> phProfiles = handlePhIds(post.getText());
        Set<MdProfile> mdProfiles = handleMdIds(post.getText());


        Publication publication = publicationService.createPublicationFromVkPost(post);

        phProfiles.forEach(phProfile -> userProfileService.addPublicationToPhProfile(publication, phProfile));
        mdProfiles.forEach(mdProfile -> userProfileService.addPublicationToMdProfile(publication, mdProfile));

        em.flush();
    }


    private Set<PhProfile> handlePhIds(final String rowText) {
        final Set<Long> phUserVkId = postRegexTextCleaner.getPhIds(rowText);
        return phUserVkId.stream()
                .map(id -> vkRestClient.getVKUserDetails(id))
                .map(vkUserDTO -> userProfileService.getOrRegisterPhProfileUser(vkUserDTO))
                .collect(Collectors.toSet());

    }

    private Set<MdProfile> handleMdIds(final String rowText) {
        final Set<Long> mdVkIds = postRegexTextCleaner.getMdIds(rowText);
        return mdVkIds.stream()
                .map(id -> vkRestClient.getVKUserDetails(id))
                .map(vkUserDTO -> userProfileService.getOrRegisterMdProfileUser(vkUserDTO))
                .collect(Collectors.toSet());

    }
}
