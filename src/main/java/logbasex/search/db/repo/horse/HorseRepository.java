package logbasex.search.db.repo.horse;

import logbasex.search.db.dto.horse.HorseRequest;
import logbasex.search.db.dto.horse.HorseResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface HorseRepository {
	List<HorseResponse> findByParams(Set<String> index, String title) throws IOException;
	
	HorseResponse create(HorseRequest request);
}
