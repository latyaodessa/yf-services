package yf.core.elastic;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GenericElasticHitsToObjectExecutor {

    @Inject
    private GenericElasticToObjectConvertor elasticToObjectConvertor;

    public <T> List<T> execute(final SearchResponse res,
                               final Class<T> cls) {
        List<T> dtos = new ArrayList<>();

        SearchHit[] hits = res.getHits()
                .getHits();

        for (SearchHit hit : hits) {
            T dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(),
                    cls);
            if (dto != null) {
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
