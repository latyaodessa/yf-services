package yf.post;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortOrder;

import yf.core.PropertiesReslover;
import yf.core.elastic.ElasticSearchExecutor;
import yf.core.elastic.ElasticToObjectConvertor;
import yf.elastic.core.NativeElasticSingleton;
import yf.post.dto.PostDetailsDTO;
import yf.post.dto.PostElasticDTO;
import yf.post.dto.SharedBasicPostDTO;

@Stateless
public class PostService {
	@Inject
	ElasticSearchExecutor elasticSearchExecutor;
	@Inject
	PostConverter basicPostConverter;
	@Inject
	NativeElasticSingleton nativeElastiClient;
	@Inject
	ElasticToObjectConvertor elasticToObjectConvertor;
	@Inject
	PropertiesReslover properties;
	
	public PostDetailsDTO getPostDetailsDTO(final String index,  final String type, final String postId){
		
		GetResponse res = nativeElastiClient.getClient().prepareGet(index, type, postId).get();
		if(!res.isExists()) { return null;}
		
		PostElasticDTO searchResult = elasticToObjectConvertor.convertSingleResultToObject(res.getSourceAsString(), PostElasticDTO.class);		
		return basicPostConverter.toPostDetailsDTO(searchResult);
	}
	
	public List<SharedBasicPostDTO> getNewPostsFromTo(final String index, final String type, final int from, final int size){		
		
			SearchResponse res = nativeElastiClient.getClient().prepareSearch(index)
			        .setTypes(type)
			        .addSort("id", SortOrder.DESC)
			        .setFrom(from).setSize(size).setExplain(true)
			        .execute()
			        .actionGet();
			
			return elasticSearchExecutor.executeSearchBasicPostDTO(res);
	}
	
	public List<SharedBasicPostDTO> getTopPostsFromTo(final String index, final String type, final int from,final int size){

			SearchResponse res = nativeElastiClient.getClient().prepareSearch(index)
			        .setTypes(type)
			        .addSort("likes", SortOrder.DESC)
			        .setFrom(from).setSize(size).setExplain(true)
			        .execute()
			        .actionGet();
	
		return elasticSearchExecutor.executeSearchBasicPostDTO(res);
	}
	
	public List<PostElasticDTO> getElasticTopPostsFromTo(final String index, final String type, final int from,final int size){

		SearchResponse res = nativeElastiClient.getClient().prepareSearch(index)
		        .setTypes(type)
		        .addSort("likes", SortOrder.DESC)
		        .setFrom(from).setSize(size).setExplain(true)
		        .execute()
		        .actionGet();

	return elasticSearchExecutor.executePostElasticDTO(res);
}
	
	public List<SharedBasicPostDTO> searchPosts(String query){
		
		if(StringUtils.isEmpty(query)) return null;
				 
			SearchResponse res = nativeElastiClient.getClient().prepareSearch("*",
																			"-*"+properties.get("elastic.index.native.top"),
																			"-*" +properties.get("elastic.index.sets.top"))
					.setQuery(QueryBuilders.matchPhraseQuery("text", query))      
			        .addSort("id", SortOrder.DESC)
			        .setFrom(0).setSize(100).setExplain(true)
			        .execute()
			        .actionGet();
		
			return elasticSearchExecutor.executeSearchBasicPostDTO(res);
	}
}
