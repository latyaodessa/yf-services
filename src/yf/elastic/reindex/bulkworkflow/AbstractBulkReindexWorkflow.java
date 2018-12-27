package yf.elastic.reindex.bulkworkflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public abstract class AbstractBulkReindexWorkflow<T, V> {

    protected static final String FULL_TEXT_ANALYZER = "full-text";
    protected static final String NGRAM_TOKINIZER = "full_text_ngrams";
    protected static final Logger LOGGER = Logger.getLogger(AbstractBulkReindexWorkflow.class.getName());
    private static final Logger LOG = Logger.getLogger(AbstractBulkReindexWorkflow.class.getName());
    public final Map<String, String> TAG_INDEX = new HashMap<>();
    protected final Set<String> INDEX = new HashSet<String>();
    protected String TYPE = null;

    @Inject
    private NativeElasticSingleton nativeElasticClient;

    @PostConstruct
    protected abstract void initIndecies();

    public abstract void recreateIndex();

    public abstract void execute(final List<T> entities);

    public abstract void addEntityToBulk(final V dto,
                                         BulkRequestBuilder bulkRequest,
                                         BulkOptions bulkOption)
            throws JsonProcessingException;

    protected IndexRequestBuilder prepareIndex(final V dto,
                                               final String id,
                                               String index) {
        if (dto == null) {
            return null;
        }
        try {
            return index != null ? nativeElasticClient.getClient()
                    .prepareIndex(index,
                            TYPE,
                            id)
                    .setSource(new ObjectMapper().writeValueAsString(dto))
                                 : null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected Settings getIndexSettings() {

        String settings = "";

        ClassLoader classLoader = Thread.currentThread()
                .getContextClassLoader();
        try {
            settings = IOUtils.toString(classLoader.getResourceAsStream("elasticsearch_setting.json"),
                    Charset.defaultCharset());
        } catch (IOException e) {
            LOG.severe(e.getMessage());
        }

        return Settings.builder()
                .loadFromSource(settings,
                        XContentType.JSON)
                .build();
    }
}
