package logbasex.search.business.factory;

import logbasex.search.business.common.SearchBaseException;
import logbasex.search.db.dto.search.SearchItem;

import java.io.IOException;

public interface ProcessMultipleDataService {
	void create(SearchItem message) throws SearchBaseException, IOException, InterruptedException;

	void update(SearchItem message) throws SearchBaseException, IOException, InterruptedException;

	void delete (SearchItem message) throws SearchBaseException, IOException, InterruptedException;
}
