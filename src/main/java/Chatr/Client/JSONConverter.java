package Chatr.Client;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;
// uses Gson to convert object to / form JSON
public class JSONConverter {
	private static Gson parser = new Gson();

	public static String toJSON(Object obj){
		return parser.toJson(obj, obj.getClass());
	}

	public static List<String> toJSON(List<? extends Object> objs) {
		List<String> list = new ArrayList<>();
		for (Object obj : objs) {
			list.add(parser.toJson(obj, obj.getClass()));
		}
		return list;
	}


	public static <T> T fromJSON(String json, @NotNull Class<? extends Object> type){
		return (T) parser.fromJson(json, type);
	}
}
