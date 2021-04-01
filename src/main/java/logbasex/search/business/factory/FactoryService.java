package logbasex.search.business.factory;

import logbasex.search.business.common.ErrorInfo;
import logbasex.search.business.common.SearchBaseException;
import logbasex.search.business.horse.HorseIndexService;
import logbasex.search.db.dto.search.SearchItem;
import logbasex.search.db.enums.IndexEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FactoryService {
	
	private final HorseIndexService horseIndexSv;
	
	public ProcessMultipleDataService getService(SearchItem message) throws SearchBaseException {
		if (message == null || message.getIndex() == null)
			throw new SearchBaseException(ErrorInfo.BAD_REQUEST);
		if (IndexEnum.HORSE.getName().equals(message.getIndex())) {
			return horseIndexSv;
		} else {
			return null;
		}
	}
}
