import dao.Database;
import dao.GroupChatDAO;
import data.GroupChat;
import data.Message;
import data.User;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.core.Application;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GroupChatDAOTest extends JerseyTest {
    private Connection connection;
    private Statement statement;
    private GroupChatDAO groupChatDAO;

    @Before
    public void SetUp () {
        try{
            connection = Database.instance().getConnection();
            groupChatDAO = new GroupChatDAO();
            statement = connection.createStatement();

        } catch (SQLException se) {
            System.out.println("Could not connect to test-database");
            se.printStackTrace();
        }
        //DROP TABLE queries
        String dropTableUserGroupChat = "DROP TABLE IF EXISTS user_groupChat";
        String dropTableGroupChat = "DROP TABLE IF EXISTS groupChat";
        String dropTableMessage = "DROP TABLE IF EXISTS message";
        String dropTableUser = "DROP TABLE IF EXISTS user";

        //CREATE TABLE
        String createTableUser =
                "CREATE TABLE user (\n" +
                        "  userId INT(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  username VARCHAR(64) NOT NULL UNIQUE,\n" +
                        "  password TEXT,\n" +
                        "  salt BLOB,\n" +
                        "  PRIMARY KEY (userId)\n" +
                        ")";

        String createTableMessage =
                "CREATE TABLE message (\n" +
                        "  messageId INT(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  userId1 INT(11) NOT NULL,\n" +
                        "  userId2 INT(11),\n" +
                        "  timestamp TIMESTAMP NOT NULL,\n" +
                        "  messageContent VARCHAR (500) NOT NULL,\n" +
                        "  groupChatId INT(11),\n" +
                        "  PRIMARY KEY (messageId),\n" +
                        "  KEY userId1 (userId1),\n" +
                        "  KEY userId2 (userId2),\n" +
                        "  CONSTRAINT message_ibfk_3 FOREIGN KEY (userId1) REFERENCES user (userId) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                        "  CONSTRAINT message_ibfk_4 FOREIGN KEY (userId2) REFERENCES user (userId) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                        "  CONSTRAINT message_ibfk_5 foreign key (groupChatId) REFERENCES groupChat (groupChatId) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                        ")";

        String createTableGroupChat =
                "CREATE TABLE groupChat(\n" +
                        "  groupChatId INT(11) NOT NULL AUTO_INCREMENT,\n" +
                        "  groupChatName VARCHAR(64) NOT NULL,\n" +
                        "  PRIMARY KEY (groupChatId)\n" +
                        ")";

        String createTableUserGroupChat =
                "CREATE TABLE user_groupChat (\n" +
                        "  userId INT(11) NOT NULL,\n" +
                        "  groupChatId INT(11) NOT NULL,\n" +
                        "  PRIMARY KEY (userId, groupChatId),\n" +
                        "  CONSTRAINT user_groupChat_ibfk_3 FOREIGN KEY (userId) REFERENCES user (userId) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                        "  CONSTRAINT user_groupChat_ibfk_4 FOREIGN KEY (groupChatId) REFERENCES groupChat (groupChatId) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                        ")";

        //INSERT INTO statements
        String insertUser = "INSERT INTO user (username) VALUES ('username')";
        String insertUser2 = "INSERT INTO user (username) VALUES ('username2')";

        String insertMessage = "INSERT INTO message (userId1, userId2, TIMESTAMP , messageContent) VALUES (1, 2, NOW(), 'test message')";
        String insertMessage2 = "INSERT INTO message (userId1, userId2, TIMESTAMP, messageContent) VALUES (2, 1, NOW(), 'test response message')";

        String insertGroupChat = "INSERT INTO groupChat (groupChatName) VALUES ('groupchat')";

        String insertUserGroupChat = "INSERT INTO user_groupChat (userId, groupChatId) VALUES (1, 1)";

        String insertGroupChatMessage = "INSERT INTO message (userId1, TIMESTAMP , messageContent, groupChatId) VALUES (1, NOW(), 'test groupchat message', 1)";

        try {
            // execute queries
            statement.executeUpdate(dropTableMessage);
            statement.executeUpdate(dropTableUserGroupChat);
            statement.executeUpdate(dropTableGroupChat);
            statement.executeUpdate(dropTableUser);
            statement.executeUpdate(createTableUser);
            statement.executeUpdate(createTableGroupChat);
            statement.executeUpdate(createTableUserGroupChat);
            statement.executeUpdate(createTableMessage);

            // execute insert updates
            statement.executeUpdate(insertUser);
            statement.executeUpdate(insertUser2);
            statement.executeUpdate(insertMessage);
            statement.executeUpdate(insertMessage2);
            statement.executeUpdate(insertGroupChat);
            statement.executeUpdate(insertUserGroupChat);
            statement.executeUpdate(insertGroupChatMessage);

        } catch (SQLException se) {
            System.out.println("Error: insert statements");
            se.printStackTrace();

        } finally {
            try {
                statement.close();

            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @After
    public void tearDown () {
        try {
            groupChatDAO = null;
            connection.close();

        } catch (SQLException se) {
            System.out.println("disconnecting failed");
            se.printStackTrace();
        }
    }

    @Override
    public Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        forceSet(TestProperties.CONTAINER_PORT, "0");
        return new ResourceConfig(GroupChatDAO.class);
    }

    @Test
    public void testAddGroupChat() {
        GroupChat groupChat = new GroupChat();
        groupChat.setGroupChatName("test groupchat");

        groupChatDAO.addGroupChat(groupChat);

        GroupChat groupChatFromDatabase = groupChatDAO.getGroupChat(2);

        assertEquals(groupChat.getGroupChatName(), groupChatFromDatabase.getGroupChatName());
        assertEquals(groupChat.getGroupChatId(), groupChatFromDatabase.getGroupChatId());
    }

    @Test
    public void testGetGroupChat() {
        final GroupChat expectedResult = new GroupChat(1, "groupchat");

        final GroupChat result = groupChatDAO.getGroupChat(1);

        assertEquals(expectedResult.getGroupChatId(), result.getGroupChatId());
        assertEquals(expectedResult.getGroupChatName(), result.getGroupChatName());
    }

    @Test
    public void testGetGroupChatByUserId() {
        final List<GroupChat> result1 = groupChatDAO.getGroupChatByUserId(1);
        final List<GroupChat> result2 = groupChatDAO.getGroupChatByUserId(2);

        assertEquals("User 1 should have 1 Groupchat", 1, result1.size());
        assertEquals("User 2 should have 0 Groupchats", 0, result2.size());
    }

    @Test
    public void testGetGroupChatMessages() {
        ArrayList<String> expectedResult = new ArrayList<>();
        expectedResult.add("test groupchat message");

        ArrayList<Message> result = groupChatDAO.getGroupChatMessages(1);
        ArrayList<String> resultStrings = new ArrayList<>();

        for (Message message : result) resultStrings.add(message.getMessageContent());

        assertEquals(expectedResult, resultStrings);
    }

    @Test
    public void testAddMessage() {
        final int groupChatId = 1;
        Message message = new Message();
        message.setUserId1(1);
        message.setTimestamp(new Timestamp(1220227200L * 1000));
        message.setMessageContent("test add message groupchat");
        message.setGroupChatId(groupChatId);

        ArrayList<Message> beforeAddMessage = groupChatDAO.getGroupChatMessages(groupChatId);
        groupChatDAO.addMessage(groupChatId, message);
        ArrayList<Message> afterAddMessage = groupChatDAO.getGroupChatMessages(groupChatId);

        assertNotEquals(beforeAddMessage.size(), afterAddMessage.size());
    }

    @Test
    public void testGetGroupChatUsers() {
        final int groupChatId = 1;
        ArrayList<String> expected = new ArrayList<>();
        expected.add("username");

        final ArrayList<User> result = groupChatDAO.getGroupChatUsers(groupChatId);
        ArrayList<String> resultUsernames = new ArrayList<>();

        for (User user : result) resultUsernames.add(user.getUsername());

        assertEquals(expected, resultUsernames);
    }
}
