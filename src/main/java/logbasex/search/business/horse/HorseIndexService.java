package logbasex.search.business.horse;

import logbasex.search.business.common.SearchBaseException;
import logbasex.search.business.factory.ProcessMultipleDataAbstractService;
import logbasex.search.business.index.IndexDocumentService;
import logbasex.search.db.dto.search.SearchItem;
import logbasex.search.db.entities.horse.Horse;
import logbasex.search.db.enums.IndexEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HorseIndexService extends ProcessMultipleDataAbstractService {
	
	private final IndexDocumentService indexDocumentSv;
	
	@Override
	public void create(SearchItem message) throws SearchBaseException, IOException, InterruptedException {
		List<Horse> horses = Collections.singletonList(new Horse(String.join("", message.getIds()), "Logbasex"));
		
		List<Map<String, Object>> horseDataIndex = horses
				.stream()
				.map(Horse::toIndexData)
				.collect(Collectors.toList());
		
		indexDocumentSv.createMultipleDocs(horseDataIndex, IndexEnum.HORSE.getName());
	}
	
	@Override
	public void update(SearchItem message) throws SearchBaseException, IOException, InterruptedException {
	
	}
	
	@Override
	public void delete(SearchItem message) throws SearchBaseException, IOException, InterruptedException {
	
	}
}
