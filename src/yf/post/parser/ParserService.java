package yf.post.parser;

import yf.core.PropertiesReslover;
import yf.elastic.reindex.ElasticBulkFetcher;
import yf.post.entities.Post;
import yf.post.parser.dto.PostDTO;
import yf.post.parser.rest.client.ParserRestClient;
import yf.post.parser.workflow.PostParserWorkflow;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class ParserService {
    @Inject
    private PostParserWorkflow postWorkflow;
    @Inject
    private ParserRestClient parserRestClient;
    @Inject
    private ElasticBulkFetcher elasticBulkFetcher;
    @Inject
    private PropertiesReslover properties;

    private static final Logger LOG = Logger.getLogger(ParserService.class.getName());
    private static final long YF_GROUP_ID = 26020797;


    public List<PostDTO> triggerPostParser(int firstpage, int lastpage) {
        List<PostDTO> postDTOList = new ArrayList<>();

        for (int i = firstpage; i + 100 <= lastpage; i += 100) {
            postDTOList.addAll(parserRestClient.parseAllPages(YF_GROUP_ID, i, i + 100));
        }
        postWorkflow.saveNewPostData(postDTOList);
        return postDTOList;

    }

    public void triggerPostParserForNewPosts() {
        List<PostDTO> postDTOList = parserRestClient.parseAllPages(YF_GROUP_ID, 0, 100);
        postWorkflow.saveUpdateNewEntry(postDTOList);
    }

    public void getAndSaveWeeklyTop() {
        postWorkflow.saveUpdateWeeklyTop();
    }

    public void addVkPostAndParticipantsToPublication(final Long postId) {
        postWorkflow.addVkPostAndParticipantsToPublication(postId);
    }


    public void parseAllVkToPublished() {
        List<Post> entities;
        int offset = 0;

        do {
            entities = elasticBulkFetcher.fetchAllModels(offset, Post.class);

            if (entities == null || entities.isEmpty()) {
                break;
            }
            entities.stream()
                    .filter(post -> post.getText().contains(properties.get("tag.vk.native")))
                    .forEach(post -> postWorkflow.addVkPostAndParticipantsToPublication(post.getId()));

            offset += entities.size();

            LOG.info(String.format("Bulk Parsing: Already parsed %s posts", offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
    }


}
