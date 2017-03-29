package Chatr.Helper;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * uses GSON Library to serialize / deserialize Objects to from JSON
 */
public class JSONTransformer {
	private static Gson parser = new Gson();

	/**
	 * converts the Object to JSON
	 *
	 * @param obj Object to be converted
	 * @return JSON representation of the Object
	 */
	public static String toJSON(Object obj) {
		return parser.toJson(obj, obj.getClass());
	}

	/**
	 * converts a List of Objects to JSON
	 *
	 * @param objs List of Objects to be converted
	 * @return List of JSON representation of the Objects
	 */
	public static List<String> toJSON(List<? extends Object> objs) {
		List<String> list = new ArrayList<>();
		for (Object obj : objs) {
			list.add(toJSON(obj));
		}
		return list;
	}

	/**
	 * converts a JSON String to an Object
	 *
	 * @param json JSON representation of the Object
	 * @param type target class of the Object
	 * @param <T>  target return type of the Object
	 * @return Object representation of the JSON String
	 */
	public static <T> T fromJSON(String json, @NotNull Class<? extends Object> type) {
		return (T) parser.fromJson(json, type);
	}

	/**
	 * converts a List of JSON Strings to a List of Objects
	 *
	 * @param jsons JSON representation of the Objects
	 * @param type  target class of the Objects
	 * @param <T>   target return type of the Objects
	 * @return Object representation of the JSON Strings
	 */
	public static <T> List<T> fromJSON(List<String> jsons, @NotNull Class<? extends Object> type) {
		List<T> out = new ArrayList<>();
		for (String json : jsons) {
			out.add(fromJSON(json, type));
		}
		return out;
	}
}
