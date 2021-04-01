package logbasex.search.db.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentTypeEnum {
	HORSE(0, "horse", "horse_index");
	
	private final Integer type;
	private final String name;
	private final String index;
}
