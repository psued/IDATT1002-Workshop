package websocket;

import dao.GroupChatDAO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import data.User;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Websocket server on server endpoint /websocket/{userId}/{username}
 */
@ServerEndpoint("/websocket/{userId}/{username}")
public class Websocket {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    /**
     * Method called when client wants to establish connection with websocket
     * @param userId added to session properties to link each session to a specific user
     * @param username added to session properties to link each session to a specific user
     * @param session information about clients session
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, @PathParam("username") String username, Session session){
        session.getUserProperties().put("userId", userId);
        session.getUserProperties().put("username", username);

        for(Session s: peers){
            try{
                //Let other connections know that a new user has connected
                s.getBasicRemote().sendText("{\"newConnection\":\"true\", \"userId\":\"" + userId + "\", \"username\":\"" + username + "\"}");
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
        peers.add(session);
    }

    /**
     * Method called when client disconnects from websocket. Removes clients session.
     * @param session information about clients session
     */
    @OnClose
    public void onClose(Session session){
        peers.remove(session);
    }

    /**
     * Method used when server receives message from client which is sent to specified user.
     * @param message message received from client
     * @param session information about clients session
     */
    @OnMessage
    public void onMessage(String message, Session session){
        //Parse message to JSONObject.
        JSONObject jsonObject = null;
        try{
            jsonObject = (JSONObject) new JSONParser().parse(message);
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        int groupChatId = -1;
        try{
            if (jsonObject != null) {
                groupChatId = ((Long) jsonObject.get("groupChatId")).intValue();
            }
        }catch(NullPointerException npe){
            npe.printStackTrace();
        }

        if(groupChatId > 0){
            sendGroupMessage(groupChatId, message, jsonObject);
        }else{
            sendPrivateMessage(message, jsonObject);
        }

    }

    /**
     * Method used to send group message
     * @param groupChatId groupChatId as int
     * @param message message as String
     * @param jsonObject jsonObject
     */
    private void sendGroupMessage(int groupChatId, String message, JSONObject jsonObject){
        GroupChatDAO groupChatDAO = new GroupChatDAO();
        ArrayList<User> users = groupChatDAO.getGroupChatUsers(groupChatId);
        String userId1 = ((String) jsonObject.get("userId1"));

        for(Session s: peers){
            String sessionUserId = (String) s.getUserProperties().get("userId");
            for(int i = 0; i < users.size(); i++){
                if(sessionUserId.equals(Integer.toString(users.get(i).getUserId())) && !sessionUserId.equals(userId1)){
                    try{
                        s.getBasicRemote().sendText(message);
                        users.remove(i);
                    }catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Method used to send private message
     * @param message message as String
     * @param jsonObject jsonObject
     */
    private void sendPrivateMessage(String message, JSONObject jsonObject){
        int userReciever = ((Long) jsonObject.get("userId2")).intValue();
        String userId2 = Integer.toString(userReciever);
        Session receiver = null;
        for(Session s: peers){
            if(s.getUserProperties().get("userId").equals(userId2)){
                receiver = s;
                break;
            }
        }
        if(receiver != null){
            try {
                receiver.getBasicRemote().sendText(message);
            }catch (IOException ioe){
                ioe.printStackTrace();
            }
        }
    }


    /**
     * Method used on error
     * @param error contains information about error
     */
    @OnError
    public void onError(Throwable error) {
        error.printStackTrace();
    }
}
