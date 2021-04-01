package logbasex.search.db.entities.horse;

import logbasex.search.business.common.DataHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Horse {
	private String id;
	private String name;
	
	public static Map<String, Object> toIndexData(Horse horse) {
		return DataHelper.toMap(horse);
	}
}
