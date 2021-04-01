package logbasex.search.db.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DatActionEnum {
	CREATE(0, "create"),
	UPDATE(1, "update"),
	DELETE(2, "delete");
	
	private final Integer id;
	private final String name;

	public static String getName(Integer id) {
		for (DatActionEnum value : DatActionEnum.values()) {
			if (value.getId().equals(id)) return value.getName();
		}
		return "";
	}
	
}
