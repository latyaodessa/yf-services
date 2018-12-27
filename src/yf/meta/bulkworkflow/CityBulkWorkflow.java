package yf.meta.bulkworkflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import yf.core.PropertiesResolover;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;
import yf.elastic.reindex.bulkworkflow.AbstractBulkReindexWorkflow;
import yf.meta.dtos.CityDTO;
import yf.meta.entities.City;
import yf.meta.services.CityConverter;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class CityBulkWorkflow extends AbstractBulkReindexWorkflow<City, CityDTO> {

    @Inject
    private PropertiesResolover properties;
    @Inject
    private NativeElasticSingleton nativeElasticClient;
    @Inject
    private ElasticWorkflow elasticWorkflow;
    @Inject
    private CityConverter cityConverter;

    @Override
    @PostConstruct
    protected void initIndecies() {
        INDEX.add(properties.get("elastic.index.city"));
        TYPE = properties.get("elastic.type.meta");
    }

    public XContentBuilder createTypeMapping() {

        try {
            return jsonBuilder().startObject()
                    .startObject(properties.get("elastic.type.meta"))
                    .startObject("properties")
                    .startObject("titleRu")
                    .field("type",
                            "string")
                    .field("analyzer",
                            NGRAM_TOKINIZER)
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type",
                            "keyword")
                    .field("ignore_above",
                            256)
                    .endObject()
                    .endObject()
                    .endObject()
                    .startObject("titleEn")
                    .field("type",
                            "string")
                    .field("analyzer",
                            NGRAM_TOKINIZER)
                    .startObject("fields")
                    .startObject("keyword")
                    .field("type",
                            "keyword")
                    .field("ignore_above",
                            256)
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void recreateIndex() {
        Settings settings = getIndexSettings();
        elasticWorkflow.deleteIndicies(INDEX);
        nativeElasticClient.getClient()
                .admin()
                .indices()
                .prepareCreate(properties.get("elastic.index.city"))
                .setSettings(settings)
                .addMapping(properties.get("elastic.type.meta"),
                        createTypeMapping())
                .execute()
                .actionGet();
    }

    @Override
    public void execute(List<City> entities) {
        BulkRequestBuilder bulkRequest = nativeElasticClient.getClient()
                .prepareBulk();
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

        for (City entity : entities) {
            CityDTO dto = cityConverter.toDto(entity);
            try {
                addEntityToBulk(dto,
                        bulkRequest,
                        BulkOptions.INDEX);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        BulkResponse bulkResponse = bulkRequest.execute()
                .actionGet();
        if (bulkResponse.hasFailures()) {
            LOGGER.log(java.util.logging.Level.WARNING,
                    "BULK REINDEX FAILUER");
        }

    }

    @Override
    public void addEntityToBulk(CityDTO dto,
                                BulkRequestBuilder bulkRequest,
                                BulkOptions bulkOption)
            throws JsonProcessingException {
        if (bulkOption == BulkOptions.INDEX) {
            IndexRequestBuilder indexBuilder = prepareIndex(dto,
                    elasticWorkflow.getDocumentId(dto.getId()),
                    INDEX.iterator()
                            .next());
            if (indexBuilder == null) {
                return;
            }
            bulkRequest.add(indexBuilder);
        }
    }
}
