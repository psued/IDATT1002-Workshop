import data.Message;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import resources.MessageResource;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageResourceTest extends JerseyTest {
    private int user1 = 1, user2 = 2;
    private Message message = new Message();


    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(MessageResource.class);
    }

    @Test
    public void testGetMessages(){
        Response output = target("message/"+user1+"/"+user2).request().get();
        assertEquals(200, output.getStatus());
        assertEquals("application/json",output.getHeaderString("Content-type"));

        assertNotNull(output.getEntity());
    }

    @Test
    public void testNewMessage(){
        message.setUserId1(user1);
        message.setUserId2(user2);
        message.setMessageContent("Testing newMessage method");
        Response output = target("message").request().post(Entity.entity(message, MediaType.APPLICATION_JSON_TYPE));

        assertEquals(200,output.getStatus());
        assertEquals("Testing newMessage method",message.getMessageContent());
        assertEquals("application/json",output.getHeaderString("Content-type"));
    }
}
