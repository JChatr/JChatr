package Chatr.Client;

import java.util.List;

public interface Connection {

	public void post(String json);
	public List<String> get();
}
