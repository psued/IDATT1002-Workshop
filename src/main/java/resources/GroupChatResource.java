package resources;

import java.util.ArrayList;

import dao.GroupChatDAO;
import data.GroupChat;
import data.Message;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

/**
 * GroupChat resource exposed at "/groupchat" path
 */
@Path("/groupchat")
public class GroupChatResource {

    /**
     * GET method to get one groupchat with specified groupChatId
     * @param groupChatId of the chat to GET
     * @return GroupChat
     */
    @GET
    @Path ("{groupChatId}")
    @Produces (MediaType.APPLICATION_JSON)
    public GroupChat getGroupChat(@PathParam("groupChatId") int groupChatId){
        GroupChatDAO groupChatDAO = new GroupChatDAO();
        GroupChat groupChat = groupChatDAO.getGroupChat(groupChatId);
        groupChat.setMessageList(groupChatDAO.getGroupChatMessages(groupChatId));
        groupChat.setUserList(groupChatDAO.getGroupChatUsers(groupChatId));
        return groupChat;
    }

    @GET
    @Path ("user/{userId}")
    @Produces (MediaType.APPLICATION_JSON)
    public ArrayList<GroupChat> getUserGroupChats(@PathParam("userId") int userId){
        GroupChatDAO groupChatDAO = new GroupChatDAO();
        return groupChatDAO.getGroupChatByUserId(userId);
    }

    @POST
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public GroupChat postGroupChat(GroupChat groupChat){
        GroupChatDAO groupChatDAO = new GroupChatDAO();
        return groupChatDAO.addGroupChat(groupChat);
    }

    @GET
    @Path ("{groupChatId}/message")
    @Produces (MediaType.APPLICATION_JSON)
    public ArrayList<Message> getGroupChatMessages(@PathParam("groupChatId") int groupChatId){
        GroupChatDAO groupChatDAO = new GroupChatDAO();
        return groupChatDAO.getGroupChatMessages(groupChatId);
    }

    @POST
    @Path ("{groupChatId}/message")
    @Consumes (MediaType.APPLICATION_JSON)
    @Produces (MediaType.APPLICATION_JSON)
    public Message postMessage(@PathParam("groupChatId") int groupChatId, Message message){
        GroupChatDAO groupChatDAO = new GroupChatDAO();
        return groupChatDAO.addMessage(groupChatId, message);
    }
}
