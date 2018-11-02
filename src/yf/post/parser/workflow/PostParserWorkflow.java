package yf.post.parser.workflow;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

import yf.core.PropertiesResolover;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;
import yf.elastic.reindex.bulkworkflow.PostBulkWorkflow;
import yf.post.PostService;
import yf.post.dto.PostElasticDTO;
import yf.post.entities.Post;
import yf.post.parser.dto.PostDTO;
import yf.publication.PublicationDao;
import yf.publication.entities.MdProfile;
import yf.publication.entities.PhProfile;
import yf.publication.entities.Publication;
import yf.user.UserProfileService;
import yf.user.rest.VkRestClient;

@Stateless
public class PostParserWorkflow {

    private final static String PH_TAG = "photograph";
    private final static String MD_TAG = "model";
    private static final Logger LOGGER = Logger.getLogger(PostBulkWorkflow.class.getName());
    @Inject
    private NativeElasticSingleton nativeElasticClient;
    @PersistenceContext
    private EntityManager em;
    @Inject
    private ParserPostConverter postConverter;
    @Inject
    private PostService postService;
    @Inject
    private PropertiesResolover properties;
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
    private PublicationDao publicationDao;
    @Inject
    private PostRegexTextCleaner regexTextCleaner;

    public List<Post> saveNewPostData(List<PostDTO> listPostDTO) {

        final List<Post> posts = persistPosts(listPostDTO);
        em.flush();
        em.clear();
        return posts;
    }

    private List<Post> persistPosts(final List<PostDTO> postDTO) {

        List<Post> posts = postDTO.stream()
                .filter(dto -> postBulkWorkflow.getIndex(dto.getText(),
                        postBulkWorkflow.TAG_INDEX) != null)
                .map(dto -> postConverter.toEntity(dto))
                .map(entity -> {
                    em.merge(entity);
                    return entity;
                })
                .collect(Collectors.toList());

        // posts.forEach(entity -> em.merge(entity));

        return posts;

    }

    public void saveUpdateWeeklyTop() {

        Set<String> indecies = new HashSet<String>(Arrays.asList(properties.get("elastic.index.native.top"),
                properties.get("elastic.index.sets.top")));
        elasticWorkflow.deleteIndicies(indecies);

        List<PostElasticDTO> nativePosts = postService.getElasticTopPostsFromTo(properties.get("elastic.index.native"),
                properties.get("elastic.type.photo"),
                0,
                10);
        List<PostElasticDTO> setsPosts = postService.getElasticTopPostsFromTo(properties.get("elastic.index.sets"),
                properties.get("elastic.type.photo"),
                0,
                10);

        BulkRequestBuilder bulkRequest = nativeElasticClient.getClient()
                .prepareBulk();

        nativePosts.forEach(dto -> {
            try {
                postBulkWorkflow.addEntityToBulkWithCustomIndex(dto,
                        bulkRequest,
                        BulkOptions.INDEX,
                        properties.get("elastic.index.native.top"));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        setsPosts.forEach(dto -> {
            try {
                postBulkWorkflow.addEntityToBulkWithCustomIndex(dto,
                        bulkRequest,
                        BulkOptions.INDEX,
                        properties.get("elastic.index.sets.top"));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        BulkResponse bulkResponse = bulkRequest.execute()
                .actionGet();
        if (bulkResponse.hasFailures()) {
            LOGGER.log(java.util.logging.Level.WARNING,
                    "BULK ERROR");
        } else {
            LOGGER.log(java.util.logging.Level.INFO,
                    "WEEKLY TOP EXECUTED. Time: " + bulkResponse.getTook() + " number of actions: " + bulkRequest.numberOfActions());
        }

    }

    public Publication getPublicationFromPost(final Long postId) {
        return publicationDao.getPublicationByVkPostId(postId);
    }

    public Publication processNewPostToPublication(final Post post) {

        Set<PhProfile> phProfiles = parsePhsFromPost(post.getText());
        Set<MdProfile> mdProfiles = parseMdsFromPost(post.getText());

        Publication publication = generatePublication(post,
                phProfiles,
                mdProfiles);

        // creating publication_user
        phProfiles.forEach(phProfile -> {
            userProfileService.generatePublicationUserFromPhProfile(publication,
                    phProfile);

        });
        mdProfiles.forEach(mdProfile -> userProfileService.generatePublicationUserFromMdProfile(publication,
                mdProfile));

        return publication;
    }

    public Publication generatePublication(final Post post,
                                           final Set<PhProfile> phProfiles,
                                           final Set<MdProfile> mdProfiles) {
        Publication publication = new Publication();
        em.persist(publication);
        final String link = generateLinkFromMdsPhs(post,
                publication.getId(),
                phProfiles,
                mdProfiles);
        publication.setLink(link);
        publication.setLikes(0);
        publication.setVkPost(em.merge(post));
        em.merge(publication);
        em.flush();
        return publication;
    }

    private String generateLinkFromMdsPhs(final Post post,
                                          final Long publicationId,
                                          final Set<PhProfile> phProfiles,
                                          final Set<MdProfile> mdProfiles) {

        Set<String> link = new HashSet<>();

        mdProfiles.forEach(mdProfile -> {
            final String firstName = mdProfile.getUser()
                    .getFirstName();
            final String lastName = mdProfile.getUser()
                    .getLastName();
            parseNameByTag(firstName,
                    lastName,
                    link);
        });

        phProfiles.forEach(phProfile -> {
            final String firstName = phProfile.getUser()
                    .getFirstName();
            final String lastName = phProfile.getUser()
                    .getLastName();
            parseNameByTag(firstName,
                    lastName,
                    link);
        });

        // try simple text
        if (link.isEmpty()) {
            parseSimpleText(post,
                    link);
        }

        if (link.isEmpty()) {
            return String.valueOf(publicationId);
        }

        final String generatedLink = regexTextCleaner.transliterate(String.join("-",
                link)
                .toLowerCase(Locale.ROOT));

        Publication publication = publicationDao.getPublicationByLink(generatedLink);

        if (publication == null) {
            return generatedLink;
        } else {
            return String.join("-",
                    generatedLink,
                    String.valueOf(publicationId));
        }

    }

    private void parseNameByTag(final String firstName,
                                final String lastName,
                                final Set<String> link) {
        if (firstName != null && lastName != null) {
            link.add(String.join("-",
                    MD_TAG,
                    firstName.replaceAll("[^A-Za-z0-9]",
                            ""),
                    lastName.replaceAll("[^A-Za-z0-9]",
                            "")));
        }
    }

    private void parseSimpleText(final Post post,
                                 final Set<String> link) {
        PostElasticDTO dto = new PostElasticDTO();
        regexTextCleaner.getCleanedText(post,
                dto);
        if (dto.getMd_translit() != null) {
            link.add(String.join("-",
                    MD_TAG,
                    dto.getMd_translit()
                            .replaceAll("[^A-Za-z0-9]",
                                    "")));
        }
        if (dto.getPh_translit() != null) {
            link.add(String.join("-",
                    PH_TAG,
                    dto.getPh_translit()
                            .replaceAll("[^A-Za-z0-9]",
                                    "")));
        }

    }

    public Set<PhProfile> parsePhsFromPost(final String rowText) {
        final Set<Long> phUserVkId = postRegexTextCleaner.getPhIds(rowText);
        return phUserVkId.stream()
                .map(id -> vkRestClient.getVKUserDetails(id))
                .map(vkUserDTO -> userProfileService.getOrRegisterPhProfileUser(vkUserDTO))
                .collect(Collectors.toSet());

    }

    public Set<MdProfile> parseMdsFromPost(final String rowText) {
        final Set<Long> mdVkIds = postRegexTextCleaner.getMdIds(rowText);
        return mdVkIds.stream()
                .map(id -> vkRestClient.getVKUserDetails(id))
                .map(vkUserDTO -> userProfileService.getOrRegisterMdProfileUser(vkUserDTO))
                .collect(Collectors.toSet());

    }
}
