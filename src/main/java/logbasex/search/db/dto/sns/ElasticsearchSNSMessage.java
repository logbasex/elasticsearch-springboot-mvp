package logbasex.search.db.dto.sns;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
public class ElasticsearchSNSMessage {
	private Collection<String> ids;
	private String index;
	private Integer action;//0=CREATE, 1=UPDATE, 2=DELETE

	public ElasticsearchSNSMessage(Collection<String> ids, String index, Integer action) {
		this.ids = ids;
		this.index = index;
		this.action = action;
	}
}
