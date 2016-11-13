package yf.core;

import java.io.IOException;

import javax.inject.Inject;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import vk.logic.elastic.ElasticSingleton;

public class ElasticSearchExecutor {
	@Inject
	ElasticSingleton elastic;
	
	public SearchResult executeSearch(Search search){
		SearchResult result = null;
		try {
			result = elastic.getClient().execute(search);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

}
