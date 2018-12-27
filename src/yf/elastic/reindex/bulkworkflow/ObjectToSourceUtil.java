package yf.elastic.reindex.bulkworkflow;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.logging.Logger;

public class ObjectToSourceUtil<T> {

    private static final Logger LOG = Logger.getLogger(ObjectToSourceUtil.class.getName());


    public static <T> String covert(T obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            LOG.severe("Con not convert object to string " + e);
            throw new RuntimeException(e);
        }
    }
}
