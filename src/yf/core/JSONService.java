package yf.core;

import java.io.IOException;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

public class JSONService {
	
	public ObjectMapper configureMapperForUnknownProperties(){
		ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD,
                Visibility.ANY);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
            false);
		return mapper;
	}
	
	public <T> Object JSONtoObject(String JSONInput, Class<T> c){
		try {
			return configureMapperForUnknownProperties().readValue(JSONInput, c);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
