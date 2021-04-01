package logbasex.search.db.repo.horse;

import logbasex.search.business.common.DataHelper;
import logbasex.search.business.common.FieldConst;
import logbasex.search.business.search.SearchService;
import logbasex.search.business.utils.SNSPublisher;
import logbasex.search.db.dto.horse.HorseRequest;
import logbasex.search.db.dto.horse.HorseResponse;
import logbasex.search.db.dto.search.ESearchResponse;
import logbasex.search.db.dto.sns.ElasticsearchSNSMessage;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
@RequiredArgsConstructor
public class HorseRepositoryImpl implements HorseRepository {
	
	private final SearchService searchSv;
	private final SNSPublisher snsPublisher;
	
	public List<HorseResponse> findByParams(Set<String> index, String title) throws IOException {
		MultiSearchRequest searchRequest = buildSearchRequest(index, title);
		ESearchResponse searchResponse = searchSv.searchByRequest(searchRequest);
		return searchResponse.getNews();
	}
	
	@Override
	public HorseResponse create(HorseRequest request) {
		ElasticsearchSNSMessage snsMessage = new ElasticsearchSNSMessage(Collections.singletonList("230a1a4b-fa02-438e-b03a-71119e5b6f3b"), "horse_index", 0);
		snsPublisher.send(snsMessage);
		return new HorseResponse("logbasex");
	}
	
	private MultiSearchRequest buildSearchRequest(Set<String> index, String title) {
		String[] indices = DataHelper.toStringArrays(index);
		
		MultiSearchRequest searchRequestList = new MultiSearchRequest();
		SearchSourceBuilder searchSourceBuilder = buildSearchSourceBuilder(title);
		SearchRequest searchRequest = new SearchRequest(indices, searchSourceBuilder);
		searchRequestList.add(searchRequest);
		
		return searchRequestList;
	}
	
	private SearchSourceBuilder buildSearchSourceBuilder(String title) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder queryBuilder = boolQuery();
		queryBuilder.must(termQuery(FieldConst.NAME, title));
		
		searchSourceBuilder.query(queryBuilder);
		return searchSourceBuilder;
	}
}
