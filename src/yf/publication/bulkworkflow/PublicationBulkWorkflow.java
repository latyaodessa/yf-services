package yf.publication.bulkworkflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest;
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
import java.util.List;
import java.util.logging.Level;

public class PublicationBulkWorkflow extends AbstractBulkReindexWorkflow<Publication, PublicationElasticDTO> {

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

    @Override
    public void deleteIndicies() {
        elasticWorkflow.deleteIndicies(INDEX);

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
