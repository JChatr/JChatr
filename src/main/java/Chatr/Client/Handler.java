package Chatr.Client;

import Chatr.Server.Transmission;

public interface Handler {
	/**
	 * processes the incoming request.
	 * @param transmission request to process
	 */
	void process(Transmission transmission);
}
