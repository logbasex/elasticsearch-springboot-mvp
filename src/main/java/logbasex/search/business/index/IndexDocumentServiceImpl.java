package logbasex.search.business.index;

import logbasex.search.business.common.ErrorInfo;
import logbasex.search.business.common.SearchBaseException;
import logbasex.search.business.common.SearchConst;
import logbasex.search.db.enums.DatActionEnum;
import logbasex.search.db.enums.IndexEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
public class IndexDocumentServiceImpl implements IndexDocumentService {
	private static final int RETRY_ON_CONFLICT = 3;
	private final RestHighLevelClient client;
	
	
	@PostConstruct
	private void initIndexes() throws IOException {
		Set<String> indexes = IndexEnum.getAll();
		for (String index : indexes) {
			GetIndexRequest getIndexRequest = new GetIndexRequest(index);
			boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
			if (exists) continue;
			CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);

			CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
			log.info(">>>>Create Index: {}", createIndexResponse.index());
		}

	}
	
	private void processMultipleDocs(List<Map<String, Object>> docs, String index, Integer action, Set<String> deleteIds) throws SearchBaseException, IOException, InterruptedException {
		//Build bulk processor
		BulkProcessor.Listener listener = new BulkProcessor.Listener() {
			@Override
			public void beforeBulk(long executionId, BulkRequest request) {
				int numberOfActions = request.numberOfActions();
				log.info("Executing bulk [{}] with {} requests",
						executionId, numberOfActions);
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request,
								  BulkResponse response) {
				if (response.hasFailures()) {
					log.info("Bulk [{}] executed with failures", executionId);
				} else {
					log.info("Bulk [{}] completed in {} milliseconds",
							executionId, response.getTook().getMillis());
				}
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request,
								  Throwable failure) {
				log.error("Failed to execute bulk", failure);
			}
		};

		BulkProcessor bulkProcessor = BulkProcessor.builder(
				(request, bulkListener) ->
						client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
				listener).build();

		BulkProcessor.Builder builder = BulkProcessor.builder(
				(request, bulkListener) ->
						client.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
				listener);

		int bulkActions = DatActionEnum.DELETE.getId().equals(action) ? deleteIds.size() : docs.size();

		builder.setBulkActions(bulkActions);

		//Process documents with action (CREATE, UPDATE, DELETE)
		processDocsByAction(action, bulkProcessor, docs, index, deleteIds);

		boolean terminated = bulkProcessor.awaitClose(30L, TimeUnit.SECONDS);

		if (terminated) {
			log.info("All bulk requests completed");
		} else {
			log.info("process requests time out");
		}
	}

	private void processDocsByAction(Integer action, BulkProcessor bulkProcessor, List<Map<String, Object>> docs, String index, Set<String> deleteIds) throws SearchBaseException {
		if (DatActionEnum.CREATE.getId().equals(action)) {
			createDocs(bulkProcessor, docs, index);
		} else if (DatActionEnum.UPDATE.getId().equals(action)) {
			createDocs(bulkProcessor, docs, index);
		} else if (DatActionEnum.DELETE.getId().equals(action)) {
			deleteDocs(bulkProcessor, deleteIds, index);
		}
	}

	private void createDocs(BulkProcessor bulkProcessor, List<Map<String, Object>> docs, String index) throws SearchBaseException {
		for (Map<String, Object> doc : docs) {
			if (doc.get(SearchConst.ID) == null) {
				throw new SearchBaseException(ErrorInfo.DOCUMENT_ID_NOT_FOUND);
			}
			String id = (String) doc.get(SearchConst.ID);
			log.info(">>>Create doc with id = {}, index = {}", id, index);

			bulkProcessor.add(new IndexRequest(index).id(id).source(doc));
		}
	}

	private void deleteDocs(BulkProcessor bulkProcessor, Set<String> ids, String index) throws SearchBaseException {
		for (String id : ids) {
			DeleteRequest request = new DeleteRequest(index, id);
			log.info("Delete doc with id = {}, index = {}", id, index);
			bulkProcessor.add(request);
		}
	}

	public void createMultipleDocs(List<Map<String, Object>> docs, String index) throws InterruptedException, SearchBaseException, IOException {
		processMultipleDocs(docs, index, DatActionEnum.CREATE.getId(), null);
	}

	public void updateMultipleDocs(List<Map<String, Object>> updateData, String index) throws InterruptedException, SearchBaseException, IOException {
		processMultipleDocs(updateData, index, DatActionEnum.UPDATE.getId(), null);
	}

	public void deleteMultipleDocs(String index, Set<String> ids) throws InterruptedException, SearchBaseException, IOException {
		processMultipleDocs(new ArrayList<>(), index, DatActionEnum.DELETE.getId(), ids);
	}


}
