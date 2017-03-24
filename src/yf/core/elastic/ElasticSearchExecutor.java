package yf.core.elastic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import yf.post.PostConverter;
import yf.post.dto.PostElasticDTO;
import yf.post.dto.SharedBasicPostDTO;

public class ElasticSearchExecutor {

	@Inject
	ElasticToObjectConvertor elasticToObjectConvertor;
	@Inject
	PostConverter basicPostConverter;
	


	public List<SharedBasicPostDTO> executeSearchBasicPostDTO(SearchResponse res){
		 List<SharedBasicPostDTO> sharedBasicPostDTO = new ArrayList<SharedBasicPostDTO>();
		 
			SearchHit[]  hits = res.getHits().getHits();
			
			for(SearchHit hit : hits) {
				PostElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PostElasticDTO.class); 
				if(dto != null){
					sharedBasicPostDTO.add(basicPostConverter.toSharedBasicPostDTO(dto));
				}
			}
			return sharedBasicPostDTO;

	}
	
	public List<PostElasticDTO> executePostElasticDTO(SearchResponse res){
		 List<PostElasticDTO> postElasticDTOList = new ArrayList<PostElasticDTO>();
		 
			SearchHit[]  hits = res.getHits().getHits();

			for(SearchHit hit : hits) {
				PostElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PostElasticDTO.class); 
				postElasticDTOList.add(dto);
			}
			return postElasticDTOList;
	}
	
	public <T>  T executeSearchIndexTypeId(GetResponse res, Class <T> c){
		return elasticToObjectConvertor.convertSingleResultToObject(res.getSourceAsString(), c); 
	}
}
