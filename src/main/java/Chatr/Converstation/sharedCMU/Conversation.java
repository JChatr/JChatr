package Chatr.Converstation.sharedCMU;

import Chatr.Converstation.*;

import java.util.LinkedList;
import java.util.*;

/**
 * Created by haags on 11.04.2017.
 */
public class Conversation {
    private String conversationID;

    private Set<User> members= new HashSet<>();
    private LinkedList<Message> messages= new LinkedList<>();

    private String conversationName;

    private User localUser;



    public Conversation(String conversationID, Set<User> members,
                        LinkedList<Message> messages, String conversationName, User localUser){

        this.conversationID= conversationID;
        this.members= members;
        this.messages= messages;
        this.conversationName= conversationName;
        this.localUser = localUser;

    }


    @Override
    public boolean equals(Object o) {
        return Objects.equals(conversationID, o.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(conversationID);
    }

    @Override
    public String toString() {
        return this.conversationID;
    }


}
