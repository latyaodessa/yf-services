package yf.core.elastic;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class GenericElasticToObjectConvertor {

    public <T> T convertSingleResultToObject(String searchResult, Class<T> cls) {

        try {
            return new ObjectMapper().readValue(searchResult, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
