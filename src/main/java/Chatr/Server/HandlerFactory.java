package Chatr.Server;

import Chatr.Helper.Enums.Request;
import Chatr.Model.User;

import java.util.Set;

/**
 * Created by haags on 10.06.2017.
 */
public class HandlerFactory{
    
    public static Handler getInstance(Request requestType) {

        switch (requestType){
            case MESSAGE:{
                return new MessageHandler();
                break;
            }

            case CONVERSATION:{
                return new ConversationHandler();
                break;
            }

            case USER:{
                return new UserHandler();
                break;
            }
        }


    }

    private static class MessageHandler extends Handler{
        @Override
        protected void process(Transmission request) {
            senderID= request.getLocalUserID();
            switch (request.getCRUD()){
                case CREATE:{
                    database.updateMessage(request.getConversationID(), request.getMessage());
                    Set<User> members = database.findConversationUsers(request.getConversationID());
                    for (User member: members) {
                        if(member.getUserID()!=senderID){
                                responses.add(request.setLocalUserID(member.getUserID()));
                        }
                    }

                }
            }
            
        }
    }
    
    private static class ConversationHandler extends Handler{
        @Override
        protected void process(Transmission request) {
            
        }
    }
    
    private static class UserHandler extends Handler{
        @Override
        protected void process(Transmission request) {
            
        }
    }
}
