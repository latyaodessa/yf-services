package yf.core;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONService {

    private static final Logger LOG = Logger.getLogger(JSONService.class.getName());


    public Object JSONtoObject(String JSONInput, Class c) {
        try {
            return configureMapperForUnknownProperties().readValue(JSONInput, c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String objectToJSON(final Object dto) {
        try {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        } catch (IOException e) {
            LOG.severe("Could not parse content dto");
        }
        return null;
    }

    private ObjectMapper configureMapperForUnknownProperties() {
        ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD,
                Visibility.ANY);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        return mapper;
    }

    public String getStringFromJsonObject(final JSONObject jsonObject, final String field) {
        if (jsonObject.has(field)) {
            try {
                return jsonObject.getString(field);
            } catch (JSONException e) {
                LOG.log(Level.SEVERE, "JSON PARSE ERROR: " + e + jsonObject);
            }
        }
        return null;
    }

    public Long getLongFromJsonObject(final JSONObject jsonObject, final String field) {
        if (jsonObject.has(field)) {
            try {
                return jsonObject.getLong(field);
            } catch (JSONException e) {
                LOG.log(Level.SEVERE, "JSON PARSE ERROR: " + e + jsonObject);
            }
        }
        return null;
    }

    public Integer getIntegerFromJsonObject(final JSONObject jsonObject, final String field) {
        if (jsonObject.has(field)) {
            try {
                return jsonObject.getInt(field);
            } catch (JSONException e) {
                LOG.log(Level.SEVERE, "JSON PARSE ERROR: " + e + jsonObject);
            }
        }
        return null;
    }

    public JSONObject getJsonObjectFromJsonObject(final JSONObject jsonObject, final String field) {
        if (jsonObject.has(field)) {
            try {
                return jsonObject.getJSONObject(field);
            } catch (JSONException e) {
                LOG.log(Level.SEVERE, "JSON PARSE ERROR: " + e + jsonObject);
            }
        }
        return null;
    }

}
