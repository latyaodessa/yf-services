package yf.elastic.reindex.bulkworkflow;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

import yf.elastic.reindex.BulkOptions;

public abstract class AbstractBulkReindexWorkflow<T, V> {
	
	public static final Logger LOGGER = Logger.getLogger(PostBulkWorkflow.class.getName());
	public final Map<String,String> TAG_INDEX  = new HashMap<String,String>();
	public final Set<String> INDEX  = new HashSet<String>();
	public String TYPE = null;
	
	 protected abstract void initIndecies();
	 public abstract void deleteIndicies();
	 public abstract void execute(final List<T> entities);
	 public abstract void addEntityToBulk(final V dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption) throws JsonProcessingException;
	 protected abstract IndexRequestBuilder prepareIndex(final V dto, final String id, String index);
	 protected abstract UpdateRequestBuilder prepareUpdateIndex(final V dto, final String id);
	 protected abstract DeleteRequestBuilder prepareDeleteIndex(final V dto, final String id);
	 public abstract  String getIndex(final T entity,final Map <String, String> tag_index);
}
