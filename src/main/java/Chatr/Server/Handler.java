package Chatr.Server;

import Chatr.Server.Database.Database;

import java.util.List;

/**
 * Created by haags on 10.06.2017.
 */
public abstract class Handler {
    protected Database database;

    protected Transmission request;
    protected List<Transmission> responses;

    protected String senderID;


    Handler(){
        this.database =Database.getCachedDatabase();
    }

    protected abstract void process(Transmission request);

}
