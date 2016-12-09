package yf.elastic.reindex;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import yf.post.entities.Post;
import yf.user.entities.User;

public class ReindexWorkflow {
	
	@Inject
	private PostBulkWorkflow postReindexWorkflow;
//	@Inject
//	private UserBulkWorkflow userBulkWorkflow;
	@PersistenceContext
	private EntityManager em;
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
	
	
	public boolean reindexUsers(){
		
		List <User> entities;
		int offset = 0;
		
//		userBulkWorkflow.deleteIndicies();
        
        do {
            entities = elasticBulkFetcher.fetchAllModels(offset, User.class);
            
            if (entities == null || entities.isEmpty()) {
                break;
            }
//            postReindexWorkflow.execute(entities);
            offset += entities.size();
            
            LOG.info(String.format("Bulk Updating: Already updated %s posts", offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
		return true;
	}
	
}
