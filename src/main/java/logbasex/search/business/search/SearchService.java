package logbasex.search.business.search;

import logbasex.search.db.dto.search.ESearchResponse;
import org.elasticsearch.action.search.MultiSearchRequest;

import java.io.IOException;

public interface SearchService {
	ESearchResponse searchByRequest(MultiSearchRequest multiSearchRequest) throws IOException;
}
