package yf.elastic.reindex;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import vk.logic.entities.Post;

public class ReindexWorkflow {
	
	@Inject
	private PostReindexWorkflow postReindexWorkflow;
	@PersistenceContext
	private EntityManager em;
	@Inject 
	private ElasticBulkFetcher elasticBulkFetcher;

	public static final Logger LOG = Logger.getLogger(ReindexWorkflow.class.getName());

	

	

	
	public boolean reindexPosts(){
		
		
		List <Post> entities;
		int offset = 0;
		
        do {
            entities = elasticBulkFetcher.fetchAllModels(offset);
            
            if (entities == null || entities.isEmpty()) {
                break;
            }
            postReindexWorkflow.execute(entities);
            

            offset += entities.size();
            
            
            LOG.info(String.format("Bulk Updating: Already updated %s posts", offset));
        } while (entities.isEmpty() || entities.size() >= ElasticBulkFetcher.getDefaultBulkSize());
        
		
		return true;
	}
	


	
	


}
