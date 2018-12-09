package yf.meta.services;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;
import yf.core.PropertiesResolover;
import yf.core.elastic.GenericElasticHitsToObjectExecutor;
import yf.elastic.core.NativeElasticSingleton;
import yf.meta.dtos.CityDTO;
import yf.meta.dtos.CountryDTO;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class MetaService {

    private static final String REGEX_SPEC_CHARACTER_CLEANER = "[^a-zA-Z а-яА-Я]";

    @Inject
    private NativeElasticSingleton nativeElastiClient;
    @Inject
    private PropertiesResolover properties;
    @Inject
    private GenericElasticHitsToObjectExecutor elasticExecutor;

    public List<CountryDTO> searchCountry(final String countrySearchText) {

        String cleanedQuery = countrySearchText.replaceAll(REGEX_SPEC_CHARACTER_CLEANER, "");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (!countrySearchText.isEmpty()) {
            boolQuery.should(QueryBuilders.matchQuery("titleRu", cleanedQuery));
            boolQuery.should(QueryBuilders.matchQuery("titleEn", cleanedQuery));
            boolQuery.minimumShouldMatch(1);
        }


        SearchRequestBuilder builder = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.country"))
                .setQuery(boolQuery);

        if (countrySearchText.isEmpty()) {
            builder.addSort("titleRu.raw", SortOrder.ASC).setSize(500);
        }
        SearchResponse res = builder.execute()
                .actionGet();

        return elasticExecutor.execute(res, CountryDTO.class);
    }

    public List<CityDTO> searchCitiesOfCountry(final String countryId, final String countrySearchText) {

        String cleanedQuery = countrySearchText.replaceAll(REGEX_SPEC_CHARACTER_CLEANER, "");

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("countryId", countryId));
        if (!countrySearchText.isEmpty()) {
            boolQuery.should(QueryBuilders.matchQuery("titleRu", cleanedQuery));
            boolQuery.should(QueryBuilders.matchQuery("titleEn", cleanedQuery));
            boolQuery.minimumShouldMatch(1);
        }


        SearchRequestBuilder builder = nativeElastiClient.getClient().prepareSearch(properties.get("elastic.index.city"))
                .setQuery(boolQuery);
        if (countrySearchText.isEmpty()) {
            builder.addSort("titleRu.raw", SortOrder.ASC);
        }
        SearchResponse res = builder.execute()
                .actionGet();

        return elasticExecutor.execute(res, CityDTO.class);
    }
}
