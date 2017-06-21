package Chatr.Client;

import Chatr.Server.Transmission;

import java.util.EventListener;

/**
 * Created by haags on 28.05.2017.
 */
interface ConnectionListener extends EventListener{

    void notify(ConnectionEvent e);

}
