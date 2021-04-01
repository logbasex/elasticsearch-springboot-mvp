package logbasex.search.db.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum IndexEnum {
	HORSE(0, "horse_index", ContentTypeEnum.HORSE.getName());

	private final Integer id;
	private final String name;
	private final String contentType;
	
	public static String getName(Integer type) {
		for (IndexEnum value : IndexEnum.values()) {
			if (value.getId().equals(type)) return value.getName();
		}
		return "";
	}

	public static String getNameByContentType(String contentType) {

		for (IndexEnum value : IndexEnum.values()) {
			if (value.getContentType() == null) continue;
			if (value.getContentType().equals(contentType)) return value.getName();
		}
		return "";
	}

	public static Set<String> getAll() {
		Set<String> indexes = new HashSet<>();
		for (IndexEnum value : IndexEnum.values()) {
			indexes.add(value.getName());
		}
		return indexes;
	}
}
