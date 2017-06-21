package Chatr.Controller;

import Chatr.Controller.Manager;
import Chatr.Model.Chat;
import Chatr.Model.User;
import Chatr.Server.Transmission;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;

/**
 * Created by haags on 19.06.17.
 */
public abstract class HandlerClient {

    protected ObjectProperty<User> localUser;
    protected ListProperty<Chat> userChats;
    protected ListProperty<User> users;




    public abstract void processClient(Transmission t);



}
