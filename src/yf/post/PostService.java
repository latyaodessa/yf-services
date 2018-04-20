package yf.post;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import yf.core.PropertiesReslover;
import yf.core.elastic.ElasticSearchExecutor;
import yf.core.elastic.ElasticToObjectConvertor;
import yf.elastic.core.NativeElasticSingleton;
import yf.post.dto.PostDetailsDTO;
import yf.post.dto.PostElasticDTO;
import yf.post.dto.SharedBasicPostDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class PostService {
    @Inject
    private ElasticSearchExecutor elasticSearchExecutor;
    @Inject
    private PostConverter basicPostConverter;
    @Inject
    private NativeElasticSingleton nativeElastiClient;
    @Inject
    private ElasticToObjectConvertor elasticToObjectConvertor;
    @Inject
    private PropertiesReslover properties;

    private static final String REGEX_SPEC_CHARACTER_CLEANER = "[^a-zA-Z а-яА-Я]";

    public PostDetailsDTO getPostDetailsDTO(final String postId) {

        MultiGetResponse res = nativeElastiClient.getClient().prepareMultiGet()
                .add(properties.get("elastic.index.native"), properties.get("elastic.type.photo"), postId)
                .add(properties.get("elastic.index.sets"), properties.get("elastic.type.photo"), postId)
                .add(properties.get("elastic.index.art"), properties.get("elastic.type.photo"), postId)
                .get();

        for (MultiGetItemResponse itemResponse : res) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                PostElasticDTO searchResult = elasticToObjectConvertor.convertSingleResultToObject(response.getSourceAsString(), PostElasticDTO.class);
                return basicPostConverter.toPostDetailsDTO(searchResult);
            }
        }

        return null;
    }

    public List<SharedBasicPostDTO> getNewPostsFromTo(final String index, final String type, final int from, final int size) {

        SearchResponse res = nativeElastiClient.getClient().prepareSearch(index)
                .setTypes(type)
                .addSort("id", SortOrder.DESC)
                .setFrom(from).setSize(size).setExplain(true)
                .execute()
                .actionGet();

        return elasticSearchExecutor.executeSearchBasicPostDTO(res);
    }

    public SharedBasicPostDTO getBasicPostDtoById(final String postId) {
        MultiGetResponse res = nativeElastiClient.getClient().prepareMultiGet()
                .add(properties.get("elastic.index.native"), properties.get("elastic.type.photo"), postId)
                .add(properties.get("elastic.index.sets"), properties.get("elastic.type.photo"), postId)
                .add(properties.get("elastic.index.art"), properties.get("elastic.type.photo"), postId)
                .get();

        for (MultiGetItemResponse itemResponse : res) {
            GetResponse response = itemResponse.getResponse();
            if (response.isExists()) {
                PostElasticDTO searchResult = elasticToObjectConvertor.convertSingleResultToObject(response.getSourceAsString(), PostElasticDTO.class);
                return basicPostConverter.toSharedBasicPostDTO(searchResult);
            }
        }
        return null;
    }

    public List<SharedBasicPostDTO> getTopPostsFromTo(final String index, final String type, final int from, final int size) {

        SearchResponse res = nativeElastiClient.getClient().prepareSearch(index)
                .setTypes(type)
                .addSort("likes", SortOrder.DESC)
                .setFrom(from).setSize(size).setExplain(true)
                .execute()
                .actionGet();

        return elasticSearchExecutor.executeSearchBasicPostDTO(res);
    }

    public List<PostElasticDTO> getElasticTopPostsFromTo(final String index, final String type, final int from, final int size) {

        SearchResponse res = nativeElastiClient.getClient().prepareSearch(index)
                .setTypes(type)
                .addSort("likes", SortOrder.DESC)
                .setFrom(from).setSize(size).setExplain(true)
                .execute()
                .actionGet();

        return elasticSearchExecutor.executePostElasticDTO(res);
    }

    public List<SharedBasicPostDTO> searchPosts(final String queries) {


        if (StringUtils.isEmpty(queries)) return null;


        String[] splitedQuery = cleanAndPrepareQuery(queries);

        SearchResponse res = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.native"),
                properties.get("elastic.index.sets"),
                properties.get("elastic.index.art"))
                .setQuery(getBoolQueryForMultipleSearch("text", splitedQuery))
                .addSort("id", SortOrder.DESC)
                .setFrom(0).setSize(100).setExplain(true)
                .execute()
                .actionGet();


        return elasticSearchExecutor.executeSearchBasicPostDTO(res);
    }

    public List<SharedBasicPostDTO> searchRelated(final String queries, final String excludeId) {


        if (StringUtils.isEmpty(queries)) return null;

        String[] splitedQuery = cleanAndPrepareQuery(queries);


        SearchResponse res = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.native"),
                properties.get("elastic.index.sets"))
                .setQuery(getBoolQueryForMultipleSearch("text", splitedQuery)
                        .mustNot(QueryBuilders.termQuery("id", excludeId)))
                .addSort("id", SortOrder.DESC)
                .setExplain(true)
                .execute()
                .actionGet();


        return elasticSearchExecutor.executeSearchBasicPostDTO(res);
    }

    private BoolQueryBuilder getBoolQueryForMultipleSearch(final String field, final String[] splitedQuery) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (String query : splitedQuery) {
            boolQuery.should(QueryBuilders.matchPhraseQuery(field, query));
        }

        boolQuery.minimumNumberShouldMatch(1);

        return boolQuery;
    }

    private String[] cleanAndPrepareQuery(final String queries) {
        String cleanedQueries = queries.replaceAll(REGEX_SPEC_CHARACTER_CLEANER, "");
        String[] splitedQuery = cleanedQueries.split(" ");

        return splitedQuery;
    }
}
