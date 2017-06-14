package Chatr.Server;

import Chatr.Helper.Enums.Request;
import Chatr.Model.Chat;
import Chatr.Model.User;

import java.util.Collection;
import java.util.Set;

/**
 * Created by haags on 10.06.2017.
 */
public class HandlerFactory {

    public static Handler getInstance(Request requestType) {

        switch (requestType) {
            case MESSAGE: {
                return new MessageHandler();
            }

            case CONVERSATION: {
                return new ConversationHandler();
            }

            case USER: {
                return new UserHandler();
            }

            case CONNECT: {
                return new ConnectHandler();
            }

            case LOGIN: {
                return new LoginHandler();
            }

            default: {
                return null;
            }
        }


    }

    private static class ConnectHandler extends Handler {
        @Override
        protected Collection<Transmission> process(Transmission request) {

            Set<Chat> c = database.readUserConversations(request.getLocalUserID());
            responses.add(request.setChats(c));
            return responses;
        }
    }

    private static class MessageHandler extends Handler {
        @Override
        protected Collection<Transmission> process(Transmission request) {
            senderID = request.getLocalUserID();
            switch (request.getCRUD()) {
                case CREATE: {
                    database.updateMessage(request.getConversationID(), request.getMessage());
                    Set<User> members = database.findConversationUsers(request.getConversationID());
                    for (User member : members) {
                        if (!member.equals(senderID)) {
                            try {
                                Transmission response = (Transmission) request.clone();
                                super.responses.add(response.setLocalUserID(member.getUserID()));
                            } catch (CloneNotSupportedException e) {
                            }

                        }
                    }
                    break;
                }
                case DELETE:{
                    database.deleteMessage(request.getConversationID(), request.getMessage().getTime());
                    Set<User> members = database.findConversationUsers(request.getConversationID());
                    for (User member : members) {
                        if (!member.equals(senderID)) {
                            try {
                                Transmission response = (Transmission) request.clone();
                                super.responses.add(response.setLocalUserID(member.getUserID()));
                            } catch (CloneNotSupportedException e) {
                            }

                        }
                    }
                    break;

                }
            }

            return responses;

        }
    }

    private static class ConversationHandler extends Handler {
        @Override
        protected Collection<Transmission> process(Transmission request) {
            senderID = request.getLocalUserID();
            switch (request.getCRUD()) {
                case CREATE: {
                    database.addConversation(request.getConversationID(),
                            request.getUserIDs());
                    for (String member : request.getUserIDs()) {
                        if (!member.equals(senderID)) {
                            try {
                                Transmission response = (Transmission) request.clone();
                                super.responses.add(response.setLocalUserID(member));
                            } catch (CloneNotSupportedException e) {
                            }
                        }
                    }
                    break;
                }
                case UPDATE: {
                    database.updateConversationUsers(request.getConversationID(), request.getUserIDs());
                    for (String member : request.getUserIDs()) {
                        if (!member.equals(senderID)) {
                            try {
                                Transmission response = (Transmission) request.clone();
                                super.responses.add(response.setLocalUserID(member));
                            } catch (CloneNotSupportedException e) {
                            }
                        }
                    }
                    break;
                }
                case DELETE:{
                    Set<User> members =database.findConversationUsers(request.getConversationID());
                    database.deleteConversation(request.getConversationID());
                    for (User member : members) {
                        if (!member.equals(senderID)) {
                            try {
                                Transmission response = (Transmission) request.clone();
                                super.responses.add(response.setLocalUserID(member.getUserID()));
                            } catch (CloneNotSupportedException e) {
                            }

                        }
                    }
                    break;

                }

            }
            return responses;
        }
    }

    private static class UserHandler extends Handler {
        @Override
        protected Collection<Transmission> process(Transmission request) {
            senderID=request.getLocalUserID();
            switch (request.getCRUD()) {
                case CREATE:
                    break;
                case READ: {
                    User user = database.readUser(request.getUserID());
                    responses.add(request.reset().setUser(user));
                    break;
                }
                case UPDATE: {
                    database.updateUser(request.getUser());

                    for (User user: database.readUsers()) {
                        if(!user.getUserID().equals(senderID)){
                            try {
                                Transmission response = (Transmission) request.clone();
                                super.responses.add(response.setLocalUserID(user.getUserID()));
                            } catch (CloneNotSupportedException e) {
                            }
                        }
                    }
                }
                case DELETE:{
                    database.deleteUser(request.getUserID());
                    responses.add(request);
                }
            }
            return responses;
        }
    }

    public static class LoginHandler extends Handler {
        @Override
        protected Collection<Transmission> process(Transmission request) {
            return null;
        }


        protected Transmission processTransmission(Transmission request) {

            switch (request.getCRUD()) {
                case READ: {
                    User user = database.readUser(request.getUserID());
                    return request.setUser(user);
                }
                case CREATE: {
                    boolean status = database.addUser(request.getUser());
                    return request.setStatus(status);
                }
                default:
                    return null;
            }

        }
    }

}


