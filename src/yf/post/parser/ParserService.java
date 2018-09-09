package yf.post.parser;

import yf.core.PropertiesResolover;
import yf.elastic.reindex.ElasticBulkFetcher;
import yf.post.entities.Post;
import yf.post.parser.dto.PostDTO;
import yf.post.parser.rest.client.ParserRestClient;
import yf.post.parser.workflow.PostParserWorkflow;
import yf.publication.bulkworkflow.PublicationBulkWorkflow;
import yf.publication.entities.Publication;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class ParserService {
    @Inject
    private PostParserWorkflow postWorkflow;
    @Inject
    private ParserRestClient parserRestClient;
    @Inject
    private ElasticBulkFetcher elasticBulkFetcher;
    @Inject
    private PropertiesResolover properties;
    @Inject
    private PublicationBulkWorkflow publicationBulkWorkflow;

    private static final Logger LOG = Logger.getLogger(ParserService.class.getName());
    private static final long YF_GROUP_ID = 26020797;


    public List<PostDTO> triggerPostParser(int firstpage, int lastpage) {
        List<PostDTO> postDTOList = new ArrayList<>();

        for (int i = firstpage; i + 100 <= lastpage; i += 100) {
            postDTOList.addAll(parserRestClient.parseAllPages(YF_GROUP_ID, i, i + 100));
        }
        final List<Post> posts = postWorkflow.saveNewPostData(postDTOList);

        List<Publication> publications = posts
                .stream()
                .map(post ->
                        postWorkflow.processNewPostToPublication(post))
                .collect(Collectors.toList());

        publicationBulkWorkflow.execute(publications);

        return postDTOList;

    }

    public void triggerPostParserForNewPosts() {
        List<PostDTO> postDTOList = parserRestClient.parseAllPages(YF_GROUP_ID, 0, 50);
        final List<Post> posts = postWorkflow.saveNewPostData(postDTOList);


        final List<Publication> publications = posts.stream()
                .map(post -> {
                    final Publication publicationFromPost = postWorkflow.getPublicationFromPost(post.getId());
                    if (publicationFromPost == null) {
                        return postWorkflow.processNewPostToPublication(post);
                    }
                    return publicationFromPost;
                })
                .collect(Collectors.toList());

        publicationBulkWorkflow.execute(publications);

    }

    public void getAndSaveWeeklyTop() {
        postWorkflow.saveUpdateWeeklyTop();
    }


    public void parseAllVkToPublished() {
        List<Post> entities;
        int offset = 0;

        do {
            entities = elasticBulkFetcher.fetchAllModels(offset, Post.class);

            if (entities == null || entities.isEmpty()) {
                break;
            }
            final List<Publication> publications = entities.stream()
                    .filter(post -> isPostContainTagForPublication(post.getText()))
                    .map(post -> postWorkflow.processNewPostToPublication(post))
                    .collect(Collectors.toList());

            publicationBulkWorkflow.execute(publications);

            offset += entities.size();

            LOG.info(String.format("Bulk Parsing: Already parsed %s posts", offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
    }

    public void parseLastVkToPublished() {

        List<Post> entities = elasticBulkFetcher.fetchAllModels(0, Post.class);

        if (entities == null || entities.isEmpty()) {
            return;
        }
        final List<Publication> publications = entities.stream()
                .filter(post -> isPostContainTagForPublication(post.getText()))
                .map(post -> postWorkflow.processNewPostToPublication(post))
                .collect(Collectors.toList());

        publicationBulkWorkflow.execute(publications);

    }


    private boolean isPostContainTagForPublication(final String text) {
        return text.contains(properties.get("tag.vk.native"))
                || text.contains(properties.get("tag.vk.sets"))
                || text.contains(properties.get("tag.vk.art"));
    }

}
