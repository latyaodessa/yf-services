package yf.core.elastic;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class GenericElasticToObjectConvertor {

    public <T> T convertSingleResultToObject(String searchResult,
                                             Class<T> cls) {

        try {
            return new ObjectMapper().readValue(searchResult,
                    cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
