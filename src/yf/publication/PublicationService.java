package yf.publication;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import yf.core.PropertiesResolover;
import yf.core.elastic.ElasticSearchExecutor;
import yf.core.elastic.ElasticToObjectConvertor;
import yf.elastic.core.NativeElasticSingleton;
import yf.post.dto.SharedBasicPostDTO;
import yf.post.entities.Post;
import yf.publication.dtos.PublicationElasticDTO;
import yf.publication.dtos.PublicationTypeEnum;
import yf.publication.entities.Publication;

import javax.inject.Inject;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.idsQuery;

public class PublicationService {

    private static final String REGEX_SPEC_CHARACTER_CLEANER = "[^a-zA-Z а-яА-Я]";

    @Inject
    private PublicationWorkflow publicationWorkflow;
    @Inject
    private NativeElasticSingleton nativeElastiClient;
    @Inject
    private PropertiesResolover properties;
    @Inject
    private ElasticSearchExecutor elasticSearchExecutor;

    public Publication createPublicationFromVkPost(final Post vkPost) {
        Publication publication = publicationWorkflow.createPublicationFromVkPost(vkPost);
        return publication;
    }

    public PublicationElasticDTO getPublicationById(final String publicationId) {

        GetResponse res = nativeElastiClient.getClient().prepareGet()
                .setIndex(properties.get("elastic.index.publication"))
                .setType(properties.get("elastic.type.photo"))
                .setId(publicationId)
                .get();

        return ElasticToObjectConvertor.convertSingleResultToObject(res.getSourceAsString(), PublicationElasticDTO.class);
    }

    public PublicationElasticDTO getPublicationByVkPostId(final String postId) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("vkPost.id", postId));

        SearchResponse res = nativeElastiClient.getClient()
                .prepareSearch(properties.get("elastic.index.publication"))
                .setTypes(properties.get("elastic.type.photo"))
                .setQuery(boolQuery)
                .execute()
                .actionGet();

        return ElasticToObjectConvertor.convertSingleResultToObjectFromResonse(res, PublicationElasticDTO.class);

    }

    public PublicationElasticDTO getPublicationByLink(final String link) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("link", link));


        SearchResponse res = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.publication"))
                .setTypes(properties.get("elastic.type.photo"))
                .setQuery(boolQuery)
                .execute()
                .actionGet();

        return ElasticToObjectConvertor.convertSingleResultToObjectFromResonse(res, PublicationElasticDTO.class);
    }

    public List<SharedBasicPostDTO> getPublicationsByTypeFromTo(final PublicationTypeEnum typeEnum,
                                                                final int from,
                                                                final int size) {


        SearchRequestBuilder request = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.publication"))
                .setTypes(properties.get("elastic.type.photo"))
                .addSort("date", SortOrder.DESC)
                .setFrom(from).setSize(size).setExplain(true);


        if (typeEnum != null) {
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            boolQuery.must(QueryBuilders.matchQuery("type", typeEnum.toString()));
            boolQuery.minimumShouldMatch();

            request.setQuery(boolQuery);
        }

        SearchResponse res = request.execute()
                .actionGet();

        return elasticSearchExecutor.executePublicationSearchBasicPostDTO(res);

    }

    public List<SharedBasicPostDTO> getPublicationsByIds(final List<String> ids) {

        QueryBuilder qb = idsQuery().addIds(ids.toArray(new String[0]));


        SearchResponse res = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.publication"))
                .setTypes(properties.get("elastic.type.photo"))
                .setQuery(qb)
                .setSize(ids.size())
                .execute()
                .actionGet();


        return elasticSearchExecutor.executePublicationSearchBasicPostDTO(res);

    }


    public List<SharedBasicPostDTO> searchRelated(final PublicationElasticDTO publicationElasticDTO) {


        if (publicationElasticDTO == null) {
            return null;
        }

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.mustNot(QueryBuilders.termQuery("id", publicationElasticDTO.getId()));
        boolQuery.should(buildRelatedQueryByMdPh(publicationElasticDTO));
        boolQuery.minimumShouldMatch(1);

        SearchResponse res = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.publication"))
                .setQuery(boolQuery)
                .addSort("date", SortOrder.DESC)
                .setExplain(true)
                .execute()
                .actionGet();


        final List<SharedBasicPostDTO> relatedPubs = elasticSearchExecutor.executePublicationSearchBasicPostDTO(res);

        if (relatedPubs.size() <= 3) {
            relatedPubs.addAll(getPublicationsByTypeFromTo(null, 0, 5));
        }
        return relatedPubs;
    }

    public List<SharedBasicPostDTO> searchPublications(final String queries) {


        if (StringUtils.isEmpty(queries)) return null;

        String[] splitedQuery = cleanAndPrepareQuery(queries);

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        for (String query : splitedQuery) {
            boolQuery.should(QueryBuilders.matchPhraseQuery("mdSimple", query));
            boolQuery.should(QueryBuilders.matchPhraseQuery("phSimple", query));
        }

        boolQuery.minimumShouldMatch(1);


        SearchResponse res = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.publication"))
                .setQuery(boolQuery)
                .addSort("date", SortOrder.DESC)
                .setFrom(0).setSize(100)
                .setExplain(true)
                .execute()
                .actionGet();

        return elasticSearchExecutor.executePublicationSearchBasicPostDTO(res);
    }


    public List<SharedBasicPostDTO> getUserPublications(final String user_id, final int from, final int size) {


        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.should(buildUserMatchPhMd(user_id));
        boolQuery.minimumShouldMatch(1);

        SearchResponse res = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.publication"))
                .setTypes(properties.get("elastic.type.photo"))
                .addSort("date", SortOrder.DESC)
                .setFrom(from).setSize(size).setExplain(true)
                .setQuery(boolQuery)
                .execute()
                .actionGet();


        return elasticSearchExecutor.executePublicationSearchBasicPostDTO(res);

    }

    private BoolQueryBuilder buildUserMatchPhMd(final String userId) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        boolQuery.should(QueryBuilders.termQuery("phUsers.userId", userId));
        boolQuery.should(QueryBuilders.termQuery("mdUsers.userId", userId));

        return boolQuery;
    }


    private String[] cleanAndPrepareQuery(final String queries) {
        String cleanedQueries = queries.replaceAll(REGEX_SPEC_CHARACTER_CLEANER, "");
        return cleanedQueries.split(" ");
    }

    private BoolQueryBuilder buildRelatedQueryByMdPh(final PublicationElasticDTO publicationElasticDTO) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        if (publicationElasticDTO.getPhUsers() != null) {
            publicationElasticDTO.getPhUsers().forEach(phUser -> {
                boolQuery.should(QueryBuilders.termQuery("phUsers.profileId", phUser.getProfileId()));
            });
        }
        if (publicationElasticDTO.getMdUsers() != null) {
            publicationElasticDTO.getMdUsers().forEach(mdUser -> {
                boolQuery.should(QueryBuilders.termQuery("mdUsers.profileId", mdUser.getProfileId()));
            });
        }
        if (publicationElasticDTO.getMdSimple() != null) {
            boolQuery.should(QueryBuilders.matchPhraseQuery("mdSimple", publicationElasticDTO.getMdSimple()));
        }

        if (publicationElasticDTO.getPhSimple() != null) {
            boolQuery.should(QueryBuilders.matchPhraseQuery("phSimple", publicationElasticDTO.getPhSimple()));
        }

        return boolQuery;
    }

}
