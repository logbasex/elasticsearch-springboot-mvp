package logbasex.search.business.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import logbasex.search.db.dto.horse.HorseResponse;
import logbasex.search.db.dto.search.ESearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	
	private final RestHighLevelClient client;
	private final ObjectMapper objectMapper;
	
	@Override
	public ESearchResponse searchByRequest(MultiSearchRequest multiSearchRequest) throws IOException {
		MultiSearchResponse response = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);
		return buildSearchResponse(response);
	}
	
	private ESearchResponse buildSearchResponse(MultiSearchResponse multiSearchResponse) {
		
		ESearchResponse response = new ESearchResponse();
		
		List<HorseResponse> news = new ArrayList<>();
		for (MultiSearchResponse.Item item : multiSearchResponse) {
			if (item.getFailure() != null) {
				log.error("Search Error ", item.getFailure());
				continue;
			}
			
			SearchResponse searchResponse = item.getResponse();
			SearchHits searchHits = searchResponse.getHits();
			for (SearchHit hit : searchHits) {
				HorseResponse horseResponse = objectMapper.convertValue(hit.getSourceAsMap(), HorseResponse.class);
				news.add(horseResponse);
			}
		}
		response.setNews(news);
		return response;
	}
}
