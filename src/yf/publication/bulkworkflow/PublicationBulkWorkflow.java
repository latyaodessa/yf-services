package yf.publication.bulkworkflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import yf.core.PropertiesReslover;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;
import yf.elastic.reindex.bulkworkflow.AbstractBulkReindexWorkflow;
import yf.publication.PublicationConverter;
import yf.publication.dtos.PublicationElasticDTO;
import yf.publication.entities.Publication;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class PublicationBulkWorkflow extends AbstractBulkReindexWorkflow<Publication, PublicationElasticDTO> {

    private static final String FULL_TEXT_ANALYZER = "full-text";
    @Inject
    private PropertiesReslover properties;
    @Inject
    private ElasticWorkflow elasticWorkflow;
    @Inject
    private NativeElasticSingleton nativeElasticClient;
    @Inject
    private PublicationConverter publicationConverter;


    @PostConstruct
    protected void initIndecies() {
        INDEX.add(properties.get("elastic.index.publication"));
        TYPE = properties.get("elastic.type.photo");


    }

    private Settings.Builder getAnalyzer() {
        try {
            return Settings.builder().loadFromSource(jsonBuilder().startObject().startObject("analysis")
                    .startObject("analyzer")
                    .startObject(FULL_TEXT_ANALYZER)
                    .field("type", "custom")
                    .field("tokenizer", "whitespace")
                    .field("filter", new String[]{"lowercase"})
                    .endObject().endObject().endObject().endObject().string(), XContentType.JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    ;

    public XContentBuilder createTypeMapping() {

        try {
            return XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject(properties.get("elastic.type.photo"))
                    .startObject("properties")
                    .startObject("link")
                    .field("type", "string")
                    .field("analyzer", FULL_TEXT_ANALYZER)
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
        elasticWorkflow.deleteIndicies(INDEX);

        nativeElasticClient.getClient().admin().indices().prepareCreate(properties.get("elastic.index.publication"))
                .setSettings(getAnalyzer())
                .addMapping(properties.get("elastic.type.photo"), createTypeMapping())
                .execute().actionGet();

    }

    @Override
    public void execute(List<Publication> entities) {

        BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE); // ??

        for (Publication p : entities) {


            PublicationElasticDTO dto = publicationConverter.publicationToElasticDTO(p);


            try {
                addEntityToBulk(dto, bulkRequest, BulkOptions.INDEX);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        BulkResponse bulkResponse = bulkRequest.execute().actionGet();
        if (bulkResponse.hasFailures()) {
            LOGGER.log(Level.SEVERE, "BULK REINDEX FAILURE");
        }
    }

    @Override
    public void addEntityToBulk(PublicationElasticDTO dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption) throws JsonProcessingException {
        if (bulkOption == BulkOptions.INDEX) {
            IndexRequestBuilder indexBuilder = prepareIndex(dto, elasticWorkflow.getDocumentId(dto.getId()), INDEX.iterator().next());
            if (indexBuilder == null) {
                return;
            }
            bulkRequest.add(indexBuilder);
        }
    }
}
