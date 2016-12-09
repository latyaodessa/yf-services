package yf.elastic.reindex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class AbstractBulkReindexWorkflow<T, V> {
	
	public static final Logger LOGGER = Logger.getLogger(PostBulkWorkflow.class.getName());
	public final Map<String,String> TAG_INDEX  = new HashMap<String,String>();

	 protected abstract void initIndecies();
	 public abstract void deleteIndicies();
	 public abstract void execute(final List<T> entity);
	 public abstract void addEntityToBulk(final V dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption) throws JsonProcessingException;
	 protected abstract IndexRequestBuilder prepareIndex(final V dto, final String id, String index);
	 protected abstract UpdateRequestBuilder prepareUpdateIndex(final V dto, final String id);
	 protected abstract DeleteRequestBuilder prepareDeleteIndex(final V dto, final String id);
	 public abstract  String getIndex(final T entity,final Map <String, String> tag_index);
}
