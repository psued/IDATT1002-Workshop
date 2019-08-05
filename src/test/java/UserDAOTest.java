import dao.Database;
import dao.UserDAO;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UserDAOTest extends JerseyTest {
    private Connection connection;
    private Statement statement;
    private UserDAO userDAO;

    @Before
    public void SetUp () {
        try {
            connection = Database.instance().getConnection();
            userDAO = new UserDAO();
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
        String inserIntoUser = "INSERT INTO user (username) VALUES ('username1')";
        String inserIntoUser2 = "INSERT INTO user (username) VALUES ('username2')";


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
            statement.executeUpdate(inserIntoUser);
            statement.executeUpdate(inserIntoUser2);
            statement.executeUpdate(insertMessage);
            statement.executeUpdate(insertMessage2);
            statement.executeUpdate(insertGroupChat);
            statement.executeUpdate(insertUserGroupChat);
            statement.executeUpdate(insertGroupChatMessage);

        } catch (SQLException e) {
            System.out.println("Error: insert statements");
            e.printStackTrace();

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
            userDAO = null;
            connection.close();
            
        } catch (SQLException se) {
            System.out.println("disconnection failed");
            se.printStackTrace();
        }
    }

    @Override
    public Application configure () {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
        forceSet(TestProperties.CONTAINER_PORT, "0");
        return new ResourceConfig(UserDAO.class);
    }

    @Test
    public void testGetUsers () {
        List<User> userlist = userDAO.getUsers();
        assertEquals(userlist.get(0).getUserId(),1);
        assertEquals(userlist.get(0).getUsername(),"username1");
        assertEquals(2,userDAO.getUsers().size());
    }

    @Test
    public void testGetUserByUsername () {
        User user = userDAO.getUserByUsername("username1");
        User user2 = userDAO.getUserByUsername("username2");
        assertEquals("username1",user.getUsername());
        assertEquals("username2",user2.getUsername());
        assertNotEquals(user.getUsername(),user2.getUsername());
    }

    @Test
    public void testUpdateUser () {
        assertTrue(userDAO.editUser(1,"newuser",null));
        List<User> userlist = userDAO.getUsers();
        assertEquals(userlist.get(0).getUsername(),"newuser");
    }


    @Test
    public void testAddUser () {
        List<User> beforeNewUser = userDAO.getUsers();
        User user = new User(3,"username3", "password",userDAO.generateSalt());
        userDAO.addUser(user);
        List<User> tmp = userDAO.getUsers();
        List<String> afterNewUser = new ArrayList<>();

        for (User user1 : tmp) {
            afterNewUser.add(user1.getUsername());
        }

        String expectedUsername = "username3";
        String actualUsername = user.getUsername();
        assertEquals(expectedUsername,actualUsername);
        assertNotEquals(beforeNewUser,afterNewUser);
    }
}