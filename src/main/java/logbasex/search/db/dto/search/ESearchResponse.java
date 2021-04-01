package logbasex.search.db.dto.search;

import logbasex.search.db.dto.horse.HorseResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ESearchResponse {
	private List<HorseResponse> news = new ArrayList<>();
}
