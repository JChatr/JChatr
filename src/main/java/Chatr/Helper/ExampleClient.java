package Chatr.Helper;

/**
 * Created by haags on 02.06.2017.
 */


    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
//import org.java_websocket.drafts.Draft_6455;
    import org.java_websocket.drafts.Draft_17;
    import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

    /** This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded. */
    public class ExampleClient extends WebSocketClient {

        public ExampleClient( URI serverUri , Draft draft ) {
            super( serverUri, draft );
        }

        public ExampleClient( URI serverURI ) {
            super( serverURI );
        }

        @Override
        public void onOpen( ServerHandshake handshakedata ) {
            System.out.println( "opened connection" );
            // if you plan to refuse connection based on ip or httpfields overload: onWebsocketHandshakeReceivedAsClient
        }

        @Override
        public void onMessage( String message ) {
            System.out.println( "received: " + message );
        }

        @Override
        public void onFragment( Framedata fragment ) {
            System.out.println( "received fragment: " + new String( fragment.getPayloadData().array() ) );
        }

        @Override
        public void onClose( int code, String reason, boolean remote ) {
            // The codecodes are documented in class org.java_websocket.framing.CloseFrame
            System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) );
        }

        @Override
        public void onError( Exception ex ) {
            ex.printStackTrace();
            // if the error is fatal then onClose will be called additionally
        }

        public static void main( String[] args ) throws URISyntaxException, IOException{
            ExampleClient c = new ExampleClient( new URI( "ws://localhost:3456" ), new Draft_17() ); // more about drafts here: http://github.com/TooTallNate/Java-WebSocket/wiki/Drafts
            c.connect();
            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String in = sysin.readLine();

                c.send(in);
            }
        }

    }
