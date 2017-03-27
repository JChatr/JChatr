package Chatr.Converstation;

public class PrivateConversation {

    String conversationID =new String(); //Hash generieren, dann final
    String conversationName=new String();

    private PrivateConversation(){

    }

    static public PrivateConversation newConversation(String userName){
        PrivateConversation pCon= new PrivateConversation();

        return pCon;
    }



}
