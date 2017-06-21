package Chatr.Server;

import Chatr.Server.Database.Database;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by haags on 10.06.2017.
 */
public abstract class Handler {
    protected Database database;
    protected List<Transmission> responses= new LinkedList<>();

    protected String senderID;

    Handler(){
        database=Database.getCachedDatabase();
    }

    protected abstract Collection<Transmission> process(Transmission request);

}
