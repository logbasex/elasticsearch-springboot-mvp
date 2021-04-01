package logbasex.search.rest.horse;

import logbasex.search.business.common.Endpoints;
import logbasex.search.db.dto.ResponseObject;
import logbasex.search.db.dto.horse.HorseRequest;
import logbasex.search.db.enums.IndexEnum;
import logbasex.search.db.repo.horse.HorseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class HorseController {
	
	private final HorseRepository horseRepo;
	
	@GetMapping(Endpoints.HORSE_URL)
	public final ResponseEntity<Object> searchNews(@RequestBody HorseRequest request) {
		ResponseObject<Object> response = new ResponseObject<>();
		try {
			Set<String> index = Stream.of(IndexEnum.HORSE.getName()).collect(Collectors.toSet());
			response.setResponseData(horseRepo.findByParams(index, request.getName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(Endpoints.HORSE_URL)
	public final ResponseEntity<Object> create(@RequestBody(required = false) HorseRequest request) {
		ResponseObject<Object> response = new ResponseObject<>();
		response.setResponseData(horseRepo.create(request));
		return ResponseEntity.ok(response);
	}
	
}
