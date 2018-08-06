package yf.core.elastic;

import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import yf.post.PostConverter;
import yf.post.dto.PostElasticDTO;
import yf.post.dto.SharedBasicPostDTO;
import yf.publication.dtos.PublicationElasticDTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ElasticSearchExecutor {

    @Inject
    private ElasticToObjectConvertor elasticToObjectConvertor;
    @Inject
    private PostConverter basicPostConverter;


    public List<SharedBasicPostDTO> executeSearchBasicPostDTO(SearchResponse res) {
        List<SharedBasicPostDTO> sharedBasicPostDTO = new ArrayList<SharedBasicPostDTO>();

        SearchHit[] hits = res.getHits().getHits();

        for (SearchHit hit : hits) {
            PostElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PostElasticDTO.class);
            if (dto != null) {
                sharedBasicPostDTO.add(basicPostConverter.toSharedBasicPostDTO(dto));
            }
        }
        return sharedBasicPostDTO;

    }

    public List<SharedBasicPostDTO> executePublicationSearchBasicPostDTO(final SearchResponse res) {
        List<SharedBasicPostDTO> sharedBasicPostDTO = new ArrayList<>();

        SearchHit[] hits = res.getHits().getHits();

        for (SearchHit hit : hits) {
            PublicationElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PublicationElasticDTO.class);
            if (dto != null) {
                sharedBasicPostDTO.add(basicPostConverter.publicationToSharedBasicPostDTO(dto));
            }
        }
        return sharedBasicPostDTO;

    }

    public SharedBasicPostDTO postElasticToBasicDto(final PostElasticDTO postElasticDTO) {
        return basicPostConverter.toSharedBasicPostDTO(postElasticDTO);
    }

    public List<PostElasticDTO> executePostElasticDTO(SearchResponse res) {
        List<PostElasticDTO> postElasticDTOList = new ArrayList<PostElasticDTO>();

        SearchHit[] hits = res.getHits().getHits();

        for (SearchHit hit : hits) {
            PostElasticDTO dto = elasticToObjectConvertor.convertSingleResultToObject(hit.getSourceAsString(), PostElasticDTO.class);
            postElasticDTOList.add(dto);
        }
        return postElasticDTOList;
    }

    public <T> T executeSearchIndexTypeId(GetResponse res, Class<T> c) {
        return elasticToObjectConvertor.convertSingleResultToObject(res.getSourceAsString(), c);
    }
}
