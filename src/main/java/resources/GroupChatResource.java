package resources;

import data.GroupChat;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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
        return null;
    }

}
