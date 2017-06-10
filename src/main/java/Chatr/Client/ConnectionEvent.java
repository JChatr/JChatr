package Chatr.Client;

import Chatr.Server.Transmission;

import java.util.EventObject;
import java.util.Objects;

/**
 * Created by haags on 28.05.2017.
 */
public class ConnectionEvent extends EventObject {
    private Transmission tres;

    public ConnectionEvent(Object source, Transmission t){
        super(source);
        this.tres= t;
    }
    public Transmission getTransmission(){
        return tres;
    }
}
