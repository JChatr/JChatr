package Chatr.Connection;

import java.util.List;

/**
 * Created by max on 17.03.17.
 */
public interface Connection {
	public void post(String json);
	public List<String> get();
}
