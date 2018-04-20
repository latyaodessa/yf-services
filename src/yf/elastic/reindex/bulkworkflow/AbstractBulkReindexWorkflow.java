package yf.elastic.reindex.bulkworkflow;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;

public abstract class AbstractBulkReindexWorkflow<T, V> {
	
	public static final Logger LOGGER = Logger.getLogger(PostBulkWorkflow.class.getName());
	public final Map<String,String> TAG_INDEX  = new HashMap<>();
	public final Set<String> INDEX  = new HashSet<String>();
	public String TYPE = null;
	
	@Inject
	private NativeElasticSingleton nativeElasticClient;
	
	 protected abstract void initIndecies();
	 public abstract void deleteIndicies();
	 public abstract void execute(final List<T> entities);
	 public abstract void addEntityToBulk(final V dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption) throws JsonProcessingException;
	 protected IndexRequestBuilder prepareIndex(final V dto, final String id, String index){
			if (dto == null) { return null; }
			try {
				return index!= null ? nativeElasticClient.getClient()
										.prepareIndex(index, TYPE, id)
										.setSource(new ObjectMapper().writeValueAsString(dto))
										:null;
			} catch (JsonGenerationException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {			
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return null;
	 }
}
