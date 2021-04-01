package logbasex.search.db.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchItem {
	private Collection<String> ids;
	private String index;
	private Integer action;
}
