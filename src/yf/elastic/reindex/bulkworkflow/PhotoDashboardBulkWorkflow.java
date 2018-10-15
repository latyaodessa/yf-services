package yf.elastic.reindex.bulkworkflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy;
import yf.core.PropertiesResolover;
import yf.dashboard.postphoto.PostPhotoDashboardConverter;
import yf.dashboard.postphoto.dto.PhotoDashboardElasticDTO;
import yf.dashboard.postphoto.entities.UserSavedPhotos;
import yf.elastic.core.ElasticWorkflow;
import yf.elastic.core.NativeElasticSingleton;
import yf.elastic.reindex.BulkOptions;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

public class PhotoDashboardBulkWorkflow extends AbstractBulkReindexWorkflow<UserSavedPhotos, PhotoDashboardElasticDTO>{
	@Inject
	private PropertiesResolover properties;
	@Inject
	private ElasticWorkflow elasticWorkflow;
	@Inject
	private NativeElasticSingleton nativeElasticClient;
	@Inject
	private PostPhotoDashboardConverter postPhotoDashboardConverter;
	@Override
	@PostConstruct
	protected void initIndecies() {
		INDEX.add(properties.get("elastic.index.dashboard.saved.photo"));
		TYPE = properties.get("elastic.type.dashboard");
	}

	@Override
	public void recreateIndex() {
		elasticWorkflow.deleteIndicies(INDEX);				
	}

	@Override
	public void execute(List<UserSavedPhotos> entities) {
		BulkRequestBuilder bulkRequest = nativeElasticClient.getClient().prepareBulk();
		bulkRequest.setRefreshPolicy(RefreshPolicy.IMMEDIATE);
				
		for(UserSavedPhotos entity : entities){
			PhotoDashboardElasticDTO dto = postPhotoDashboardConverter.toPhotoDashboardElasticDTO(entity);
			try {
				addEntityToBulk(dto, bulkRequest, BulkOptions.INDEX);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		if (bulkResponse.hasFailures()) {
			LOGGER.log(java.util.logging.Level.WARNING, "BULK REINDEX FAILUER");
			}				
		
	}

	@Override
	public void addEntityToBulk(PhotoDashboardElasticDTO dto, BulkRequestBuilder bulkRequest, BulkOptions bulkOption)
			throws JsonProcessingException {
		if(bulkOption == BulkOptions.INDEX){
	    	IndexRequestBuilder indexBuilder = prepareIndex(dto, elasticWorkflow.getDocumentId(dto.getId()), INDEX.iterator().next());
			if (indexBuilder == null) { return; }
			bulkRequest.add(indexBuilder);
		}		
	}

}
