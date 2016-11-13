package yf.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import vk.parser.dto.elastic.PostElasticDTO;
import yf.core.ElasticSearchExecutor;
import yf.post.dto.PostDetailsDTO;
import yf.post.dto.SharedBasicPostDTO;

@Stateless
public class PostService {
	@Inject
	ElasticSearchExecutor elasticSearchExecutor;
	@Inject
	PostConverter basicPostConverter;
	
	public PostDetailsDTO getPostDetailsDTO(final Long postId){
		
		SearchResult result = elasticSearchExecutor.executeSearch(new Search.Builder("")
				.setParameter("_id", postId)
				.build());
		if(!result.getHits(PostElasticDTO.class).isEmpty()){
		 return	basicPostConverter.toPostDetailsDTO(result.getHits(PostElasticDTO.class).get(0).source);
		}
		return null;
		
	}
	
	public List<SharedBasicPostDTO> getNewPostsFromTo(int from, int size, String index){
		 List<SharedBasicPostDTO> sharedBasicPostDTO = new ArrayList<SharedBasicPostDTO>();
		
		SearchResult result = elasticSearchExecutor.executeSearch(new Search.Builder("")
				.addIndex(index)
				.setParameter("size", size)
				.setParameter("sort", "id:desc")
				.build());

		
		result.getHits(PostElasticDTO.class).forEach( post -> {
			sharedBasicPostDTO.add(basicPostConverter.toSharedBasicPostDTO(post.source));
		});
		
		return sharedBasicPostDTO;

	}
	

}
