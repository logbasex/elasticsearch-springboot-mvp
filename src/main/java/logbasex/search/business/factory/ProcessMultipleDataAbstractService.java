package logbasex.search.business.factory;

import logbasex.search.business.common.SearchBaseException;
import logbasex.search.db.dto.search.SearchItem;

import java.io.IOException;

public abstract class ProcessMultipleDataAbstractService implements ProcessMultipleDataService {
	public abstract void create(SearchItem message) throws SearchBaseException, IOException, InterruptedException;

	public abstract void update(SearchItem message) throws SearchBaseException, IOException, InterruptedException;

	public abstract void delete(SearchItem message) throws SearchBaseException, IOException, InterruptedException;
}
