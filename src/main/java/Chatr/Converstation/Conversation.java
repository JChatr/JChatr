package Chatr.Converstation;
import Chatr.Client.Connection;
import Chatr.Helper.HashGen;

import javax.jws.soap.SOAPBinding;
import java.util.*;

public class Conversation {

    private String conversationID =new String(); //Hash generieren, dann final
    private String conversationName=new String();
    private Set<User> members= new HashSet<>();
    private List<Message> messages= new ArrayList<>();
    private User localUser;



    private Conversation(String conversationName,Collection<User> members, User localUSer){

        this.members.addAll(members);
        this.members.add(localUSer);
        this.localUser=localUser;

        this.conversationName=conversationName;
        this.conversationID=HashGen.getID(false);

        this.create();
    }


    private Conversation(User member, User localUser){

        this.members.add(member);
        this.members.add(localUser);
        this.localUser= localUser;


        this.conversationName=member.getUserName();
        this.conversationID=HashGen.getID(false);

        this.create();
    }



    static public Conversation newConversation(User member, User localUser){
        Conversation pCon= new Conversation(member, localUser);


        return pCon;
    }


    static public Conversation  newGroupConversation(String conversationName,Collection<User> members, User localUser){

        Conversation pGCon= new Conversation(conversationName, members, localUser);

        return pGCon;
    }



    private void create(){

        List<String> memberIDs= new ArrayList<>();

        for(User member : members){
            memberIDs.add(member.getUserID());
        }

        Connection.createConversation(this.conversationID, memberIDs);
    }

    public void delete(){

        Connection.deleteConversation(this.conversationID);

    }

    public void read(){
        Connection.readConversation(this.conversationID,NULL);
    }

}
