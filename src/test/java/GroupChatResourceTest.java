import data.GroupChat;
import data.Message;
import data.User;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import resources.GroupChatResource;
import resources.UserResource;
import jakarta.ws.rs.client.Entity;

// import javax.ws.rs.client.Entity;
// import javax.ws.rs.core.Application;
// import javax.ws.rs.core.MediaType;
// import javax.ws.rs.core.Response;

import jakarta.ws.rs.core.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

public class GroupChatResourceTest extends JerseyTest {
  private final GroupChatResource groupChatResource = new GroupChatResource();
  private int groupChatId = 1;

  @Override
  public Application configure() {
    enable(TestProperties.LOG_TRAFFIC);
    enable(TestProperties.DUMP_ENTITY);
    return new ResourceConfig(GroupChatResource.class);
  }

  @Test
  public void testGetGroupChat() {
    Response output = target("groupchat/" + groupChatId).request().get();
    assertEquals("Should return status 200", 200, output.getStatus());
    assertEquals("application/json", output.getHeaderString("Content-type"));
    assertNotNull(output.getEntity());
  }

  @Test
  public void testGetGroupChatByUserId() {
    int userId = 1;
    Response output = target("groupchat/user/" + userId).request().get();
    assertEquals("Should return status 200", 200, output.getStatus());
    assertEquals("application/json", output.getHeaderString("Content-type"));
    assertNotNull(output.getEntity());
  }

  @Test
  public void testGetMessages() {
    Response output = target("groupchat/" + groupChatId + "/message").request().get();
    assertEquals("Should return status 200", 200, output.getStatus());
    assertEquals("application/json", output.getHeaderString("Content-type"));
    assertNotNull(output.getEntity());
  }

  @Test
  public void testPostGroupChat() {
    GroupChat groupChat = new GroupChat();
    ArrayList<User> users = new ArrayList<>();
    User user = new User();
    user.setUserId(3);
    user.setUsername("testPostGroupChatUser");
    UserResource userResource = new UserResource();
    userResource.newUser(user);
    users.add(user);
    groupChat.setUserList(users);

    groupChat.setGroupChatName("testPostGroupChat");

    Response output = target("/groupchat").request().post(Entity.entity(groupChat, MediaType.APPLICATION_JSON));
    assertEquals("Should return status 200",200,output.getStatus());
    assertEquals("application/json",output.getHeaderString("Content-type"));
    assertEquals("testPostGroupChat", groupChatResource.postGroupChat(groupChat).getGroupChatName());
  }

  @Test
  public void testPostMessage() {
    Message groupChatMessage = new Message();
    groupChatMessage.setUserId1(1);
    groupChatMessage.setMessageContent("testPostMessage");
    groupChatMessage.setGroupChatId(groupChatId);

    Response output = target("groupchat/" + groupChatId + "/message").request().post(Entity.entity(groupChatMessage, MediaType.APPLICATION_JSON));
    assertEquals("Should return status 200",200, output.getStatus());
    assertEquals("application/json", output.getHeaderString("Content-type"));
    assertEquals("testPostMessage", groupChatResource.postMessage(groupChatId, groupChatMessage).getMessageContent());
  }
}
