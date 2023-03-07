import dao.UserDAO;
import data.User;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.Test;
import resources.UserResource;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserResourceTest extends JerseyTest {
    private final UserResource userResource = new UserResource();
    private final UserDAO userDAO = new UserDAO();
    private User user = new User(10,"username","password",userDAO.generateSalt());
    private User user2 = new User();

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        return new ResourceConfig(UserResource.class);
    }

    @Test
    public void testNewUser(){
        Response output = target("/user").request().post(Entity.entity(user, MediaType.APPLICATION_JSON));
        assertEquals(200,output.getStatus());
        assertEquals("application/json",output.getHeaderString("Content-type"));
        //assertEquals("username", userResource.newUser(user).getUsername());
    }

    @Test
    public void testGetUsers(){
        Response output = target("/user").request().get();
        assertEquals(200, output.getStatus());
        assertNotNull(output.getEntity().toString());
        assertEquals("application/json",output.getHeaderString("Content-type"));
    }

    @Test
    public void testEditUser () {
        user2.setUserId(10);
        user2.setUsername("test");
        Response output = target("user/"+2).request().put(Entity.entity(user2, MediaType.APPLICATION_JSON));
        assertEquals(200, output.getStatus(), "Should return status 200");
        assertEquals("application/json",output.getHeaderString("Content-type"));
        assertNotEquals("testUsername",user2.getUsername());
    }
}
