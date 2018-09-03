package yf.elastic.reindex.bulkworkflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public abstract class AbstractBulkReindexWorkflow<T, V> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractBulkReindexWorkflow.class.getName());
    public final Map<String, String> TAG_INDEX = new HashMap<>();
    protected final Set<String> INDEX = new HashSet<String>();
    protected String TYPE = null;

    @Inject
    private NativeElasticSingleton nativeElasticClient;

    @PostConstruct
    protected abstract void initIndecies();

    public abstract void recreateIndex();

    public abstract void execute(final List<T> entities);

    public abstract void addEntityToBulk(final V dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption) throws JsonProcessingException;

    protected IndexRequestBuilder prepareIndex(final V dto, final String id, String index) {
        if (dto == null) {
            return null;
        }
        try {
            return index != null ? nativeElasticClient.getClient()
                    .prepareIndex(index, TYPE, id)
                    .setSource(new ObjectMapper().writeValueAsString(dto))
                    : null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
