package yf.publication;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import yf.core.PropertiesReslover;
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

public class PublicationService {

    @Inject
    private PublicationWorkflow publicationWorkflow;
    @Inject
    private NativeElasticSingleton nativeElastiClient;
    @Inject
    private PropertiesReslover properties;
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

    public List<SharedBasicPostDTO> getPublicationsByTypeFromTo(final PublicationTypeEnum typeEnum,
                                                                final int from,
                                                                final int size) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("type", typeEnum.toString()));

        boolQuery.minimumShouldMatch();

        SearchResponse res = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.publication"))
                .setTypes(properties.get("elastic.type.photo"))
                .addSort("date", SortOrder.DESC)
                .setQuery(boolQuery)
                .setFrom(from).setSize(size).setExplain(true)
                .execute()
                .actionGet();


        return elasticSearchExecutor.executePublicationSearchBasicPostDTO(res);

    }

}
