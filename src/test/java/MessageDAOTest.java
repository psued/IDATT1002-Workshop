import dao.Database;
import dao.MessageDAO;
import data.Message;
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

import static org.junit.Assert.*;


public class MessageDAOTest extends JerseyTest {
    private Connection connection;
    private MessageDAO messageDAO;
    private Statement statement;
    private int user1 = 1, user2 = 2;

    @Before
    public void SetUp () {
        try{
            connection = Database.instance().getConnection();
            messageDAO = new MessageDAO();
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
            messageDAO = null;
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
        return new ResourceConfig(MessageDAO.class);
    }


    @Test
    public void testGetMessage () {
        List<Message> messageList = messageDAO.getMessages(user1,user2);
        List<String> actualMessageList = new ArrayList<>();

        for (int i = 0; i < messageList.size(); i++) {
            actualMessageList.add(messageList.get(i).getMessageContent());
        }

        List<String> expectedMessageList = new ArrayList<>();
        expectedMessageList.add("test message");
        expectedMessageList.add("test response message");
        assertTrue(expectedMessageList.size() == actualMessageList.size() && actualMessageList.containsAll(expectedMessageList));
    }

    @Test
    public void testAddMessage () {
        List<Message> messageList = messageDAO.getMessages(user1,user2);
        List<String> beforeNewMessage = new ArrayList<>();

        for (Message message1 : messageList) {
            beforeNewMessage.add(message1.getMessageContent());
        }

        Message message = new Message(user1, user2, 3, "Testing newMessage method", new Timestamp(1220227200L * 1000));
        messageDAO.addMessage(message);
        String expectedMessage = message.getMessageContent();
        String actualMessage = "Testing newMessage method";

        messageList = messageDAO.getMessages(user1,user2);
        List<String> afterNewMessage = new ArrayList<>();
        for (Message message1 : messageList) afterNewMessage.add(message1.getMessageContent());

        assertNotEquals("Should not be equal, adding new message to list",beforeNewMessage.size(),afterNewMessage.size());
        assertEquals("Should be equal",expectedMessage,actualMessage);

    }
}