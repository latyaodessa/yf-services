package yf.post.parser;

import yf.post.parser.dto.PostDTO;
import yf.post.parser.rest.client.ParserRestClient;
import yf.post.parser.workflow.PostParserWorkflow;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class ParserService {
    @Inject
    private PostParserWorkflow postWorkflow;
    @Inject
    private ParserRestClient parserRestClient;

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


}
