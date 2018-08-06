package yf.core.elastic;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class ElasticToObjectConvertor {

		public static <T> T convertSingleResultToObject(String searchResult, Class<T> cls) {
			
			try {
				return new ObjectMapper().readValue(searchResult,cls);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;

		}
}
