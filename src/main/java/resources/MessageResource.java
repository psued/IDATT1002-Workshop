package resources;

import dao.MessageDAO;
import data.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Message resource exposed at "/message" path
 */
@Path("/message")
public class MessageResource {

    /**
     * Method handling HTTP GET requests. The returned object will be sent to the client
     * as a JSON object.
     * @param userId1 describes first user of the messages
     * @param userId2 describes second user of the messages
     * @return list of messages as JSON object
     */
    @GET
    @Path("{userId1}/{userId2}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getMessages(@PathParam("userId1") int userId1, @PathParam("userId2") int userId2){
        MessageDAO messageDAO = new MessageDAO();
        return messageDAO.getMessages(userId1, userId2);
    }

    /**
     * Method handling HTTP POST requests.
     * @param message the Message to be saved in database
     * @return the Message object as JSON response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Message newMessage(Message message){
        MessageDAO messageDAO = new MessageDAO();
        return messageDAO.addMessage(message);
    }
}
