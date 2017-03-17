package Chatr.Connection;

import com.google.gson.Gson;
import com.sun.istack.internal.NotNull;

/**
 * Created by max on 17.03.17.
 */
public class JSONConverter {

	private static Gson parser = new Gson();

	protected static String toJSON(Object obj){
		return parser.toJson(obj, obj.getClass());
	}

	protected static <T> T fromJSON(String json, @NotNull Class<? extends Object> type){
		return (T) parser.fromJson(json, type);
	}
}
