package logbasex.search.business.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class DataHelper {
	
	public static Map<String, Object> toMap(Object object) {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.convertValue(object, Map.class);
		return map;
	}
	
	public static String[] toStringArrays(Collection<String> collections) {
		return collections.toArray(new String[0]);
	}
	
	public static int[] toIntegerArrays(Collection<Integer> collections) {
		return collections.stream()
				.mapToInt(i -> i)
				.toArray();
	}
	
	public static String[] toStringLowerCaseArrays(Collection<String> collections) {
		return toStringArrays(collections.stream()
				.map(String::toLowerCase)
				.collect(Collectors.toList()));
	}
}
