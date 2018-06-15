package yf.elastic.reindex;

import yf.dashboard.postphoto.entities.UserSavedPhotos;
import yf.dashboard.postphoto.entities.UserSavedPosts;
import yf.elastic.reindex.bulkworkflow.PhotoDashboardBulkWorkflow;
import yf.elastic.reindex.bulkworkflow.PostBulkWorkflow;
import yf.elastic.reindex.bulkworkflow.PostDashboardBulkWorkflow;
import yf.post.entities.Post;

import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

public class ReindexWorkflow {
	
	@Inject
	private PostBulkWorkflow postReindexWorkflow;
	@Inject
	private PostDashboardBulkWorkflow postDashboardBulkWorkflow;
	@Inject
	private PhotoDashboardBulkWorkflow photoDashboardBulkWorkflow;
	@Inject 
	private ElasticBulkFetcher elasticBulkFetcher;

	public static final Logger LOG = Logger.getLogger(ReindexWorkflow.class.getName());
	
	public boolean reindexPosts(){
		
		List <Post> entities;
		int offset = 0;
		
        postReindexWorkflow.deleteIndicies();
        
        do {
            entities = elasticBulkFetcher.fetchAllModels(offset, Post.class);
            
            if (entities == null || entities.isEmpty()) {
                break;
            }
            postReindexWorkflow.execute(entities);
            offset += entities.size();
            
            LOG.info(String.format("Bulk Updating: Already updated %s posts", offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
		return true;
	}
	
	public boolean reindexDashboardPosts(){
		List <UserSavedPosts> entities;
		int offset = 0;
		
		postDashboardBulkWorkflow.deleteIndicies();
        
        do {
            entities = elasticBulkFetcher.fetchAllModels(offset, UserSavedPosts.class);
            
            if (entities == null || entities.isEmpty()) {
                break;
            }
            postDashboardBulkWorkflow.execute(entities);
            offset += entities.size();
            
            LOG.info(String.format("Bulk Updating: Already updated %s dashboard posts", offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
		return true;
	}
	
	public boolean reindexDashboardPhotos(){
		List <UserSavedPhotos> entities;
		int offset = 0;
		
		photoDashboardBulkWorkflow.deleteIndicies();
        
        do {
            entities = elasticBulkFetcher.fetchAllModels(offset, UserSavedPhotos.class);
            
            if (entities == null || entities.isEmpty()) {
                break;
            }
            photoDashboardBulkWorkflow.execute(entities);
            offset += entities.size();
            
            LOG.info(String.format("Bulk Updating: Already updated %s dashboard photos", offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
		return true;
	}
}
