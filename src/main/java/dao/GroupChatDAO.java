package dao;

import data.GroupChat;
import data.Message;
import data.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static dao.Database.close;


/**
 * Data access object for GroupChat
 */
public class GroupChatDAO {

    /**
     * Add a new GroupChat
     * @param groupChat GroupChat to be added
     * @return GroupChat, empty GroupChat object if unsuccessful
     */


    public GroupChat addGroupChat(GroupChat groupChat){
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try{
            connection = Database.instance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO groupChat (groupChatName) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, groupChat.getGroupChatName());

            int result = preparedStatement.executeUpdate();

            if(result == 1){
                resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    int groupChatId = resultSet.getInt(1);
                    groupChat.setGroupChatId(groupChatId);

                    ArrayList<User> users = groupChat.getUserList();
                    preparedStatement = connection.prepareStatement("INSERT INTO user_groupChat (userId, groupChatId) VALUES (?, ?) ");
                    for(User user:users){
                        preparedStatement.setInt(1, user.getUserId());
                        preparedStatement.setInt(2, groupChatId);
                        preparedStatement.executeUpdate();
                    }
                    return groupChat;
                }
            }

        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            close(connection, preparedStatement, resultSet);
        }
        return new GroupChat();
    }

    /**
     * Get a GroupChat, with given groupChatId
     * @param groupChatId groupChatId as int
     * @return requested GroupChat if found, empty GroupChat object if not found
     */


    public GroupChat getGroupChat(int groupChatId){
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        GroupChat groupChat = new GroupChat();
        try{
            connection = Database.instance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM groupChat WHERE groupChatId = ?");
            preparedStatement.setInt(1, groupChatId);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                groupChat.setGroupChatId(groupChatId);
                groupChat.setGroupChatName(resultSet.getString("groupChatName"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            close(connection, preparedStatement, resultSet);
        }
        return groupChat;
    }



    /**
     * Get all GroupChats for user with given userId
     * @param userId int userId of the user
     * @return ArrayList of GroupChats
     */


    public ArrayList<GroupChat> getGroupChatByUserId(int userId){
        ArrayList<GroupChat> groupChats = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try{
            connection = Database.instance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM groupChat WHERE groupChatId IN (SELECT groupChatId FROM user_groupChat WHERE userId = ?)");
            preparedStatement.setInt(1, userId);
            resultSet = preparedStatement.executeQuery();
            GroupChat groupChat;
            while(resultSet.next()){
                groupChat = new GroupChat();
                groupChat.setGroupChatId(resultSet.getInt("groupChatId"));
                groupChat.setGroupChatName(resultSet.getString("groupChatName"));
                groupChats.add(groupChat);
            }
            return groupChats;
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            close(connection, preparedStatement, resultSet);
        }
        return groupChats;
    }

    /**
     * Get all messages for a GroupChat
     * @param groupChatId groupChatId as int
     * @return ArrayList of Messages
     */


    public ArrayList<Message> getGroupChatMessages(int groupChatId){
        ArrayList<Message> messages = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        ResultSet resultSet = null;
        try{
            connection = Database.instance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM message WHERE groupChatId = ?");
            preparedStatement.setInt(1, groupChatId);
            resultSet = preparedStatement.executeQuery();

            Message message;
            while(resultSet.next()){
                message = new Message();
                message.setMessageId(resultSet.getInt("messageId"));
                message.setUserId1(resultSet.getInt("userId1"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                message.setTimestamp(resultSet.getTimestamp("timestamp",cal));
                message.setMessageContent(resultSet.getString("messageContent"));
                message.setGroupChatId(resultSet.getInt("groupChatId"));
                messages.add(message);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            close(connection, preparedStatement, resultSet);
        }
        return messages;
    }

    /**
     * Adds a Message to a GroupChat
     * @param groupChatId groupChatId as int
     * @param message Message object to be added to GroupChat
     * @return The message that was added
     */

    public Message addMessage(int groupChatId, Message message){
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try{
            connection = Database.instance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO message (userId1, timestamp, messageContent, groupChatId) VALUES (?, NOW(), ?, ?)");
            preparedStatement.setInt(1, message.getUserId1());
            preparedStatement.setString(2, message.getMessageContent());
            preparedStatement.setInt(3, groupChatId);

            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }finally{
            close(connection, preparedStatement, null);
        }
        return message;
    }

    /**
     * Get all users in a GroupChat
     * @param groupChatId groupChatId as int
     * @return ArrayList of Users the GroupChat
     */


    public ArrayList<User> getGroupChatUsers(int groupChatId){
        ArrayList<User> users = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try{
            connection = Database.instance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM user WHERE user.userId IN (SELECT userId FROM user_groupChat WHERE groupChatId = ?)");
            preparedStatement.setInt(1, groupChatId);
            resultSet = preparedStatement.executeQuery();

            User user;
            while(resultSet.next()){
                user = new User();
                user.setUserId(resultSet.getInt("userId"));
                user.setUsername(resultSet.getString("username"));
                users.add(user);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            close(connection, preparedStatement, resultSet);
        }
        return users;
    }

}
