package Chatr.Server;

import Chatr.Helper.CONFIG;
import javafx.beans.property.BooleanProperty;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by haags on 20.06.17.
 */
public class ClientMockUp {



    ClientMockUp() {

        try {
            WebSocketClient socketClient = new WebSocketClient(new URI(CONFIG.SERVER_ADDRESS)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {

                }

                @Override
                public void onMessage(String s) {

                }

                @Override
                public void onClose(int i, String s, boolean b) {

                }

                @Override
                public void onError(Exception e) {

                }
            };

            socketClient.connect();
        }
        catch (URISyntaxException e){
        }




    }
}
