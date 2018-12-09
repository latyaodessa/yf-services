package yf.core.elastic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

public final class ElasticToObjectConvertor {

    private ElasticToObjectConvertor() {

    }

    public static <T> T convertSingleResultToObject(final String searchResult,
                                                    final Class<T> cls) {

        try {
            return new ObjectMapper().readValue(searchResult,
                    cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static <T> T convertSingleResultToObjectFromResonse(final SearchResponse res,
                                                               final Class<T> cls) {
        List<T> dtos = new ArrayList<>();

        SearchHit[] hits = res.getHits()
                .getHits();

        for (SearchHit hit : hits) {
            dtos.add(convertSingleResultToObject(hit.getSourceAsString(),
                    cls));
        }

        if (dtos.size() > 1) {
            try {
                throw new Exception("not a unique link");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (dtos.isEmpty()) {
            return null;
        }
        return dtos.get(0);

    }
}
