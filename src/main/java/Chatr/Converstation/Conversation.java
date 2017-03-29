package Chatr.Converstation;
import Chatr.Client.Connection;
import Chatr.Helper.HashGen;

import java.util.*;

public class Conversation {

    private String conversationID =new String(); //Hash generieren, dann final
    private String conversationName=new String();
    private Set<User> members= new HashSet<>();

    private enum ConverastionState{
        CREATE, READ, UPDATE, DELETE
    }



    private Conversation(String conversationName,Collection<User> member){

        this.members.addAll(member);
    }

    private Conversation(User member){

        this.members.add(member);
    }

    static public Conversation newConversation(User member){
        Conversation pCon= new Conversation(member);

        return pCon;
    }

    static public Conversation  newGroupConversation(String conversationName,Collection<User> members){

        Conversation pGCon= new Conversation(conversationName, members);

        for(User member : pGCon.members){

        }

        Connection.createConversation(this.conversationID, this.conversationName)

        return pGCon;
    }

    private void create(){

        List<String> memberIDs= new ArrayList<>();

        for(User member : members){
            memberIDs.add(member.getUserID());
        }

        //Connection.createConversation();
    }

}
