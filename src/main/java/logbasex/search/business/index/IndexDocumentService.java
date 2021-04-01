package logbasex.search.business.index;

import logbasex.search.business.common.SearchBaseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IndexDocumentService {
	void createMultipleDocs(List<Map<String, Object>> docs, String index) throws InterruptedException, SearchBaseException, IOException;

	void updateMultipleDocs(List<Map<String, Object>> updateData, String index) throws InterruptedException, SearchBaseException, IOException;

	void deleteMultipleDocs(String index, Set<String> ids) throws InterruptedException, SearchBaseException, IOException;
}
