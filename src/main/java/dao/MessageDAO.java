package dao;

import data.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static dao.Database.close;


/**
 * Data access object for Message
 */
public class MessageDAO {

    /**
     * Method used to get all messages for a chat
     * @param userId1 ID of the user the messages are sent from
     * @param userId2 ID of the user the messages are sent to
     * @return List of messages
     */
    public List<Message> getMessages(int userId1, int userId2) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Message> messages = new ArrayList<>();

        try{
            connection = Database.instance().getConnection();
            preparedStatement = connection.prepareStatement(
                    "SELECT * FROM message WHERE (userId1 = ? AND userId2 = ?) OR (userId2 = ? AND userId1 = ?) ORDER BY `timestamp` ASC ");
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            preparedStatement.setInt(3, userId1);
            preparedStatement.setInt(4, userId2);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                Message message = new Message();
                message.setMessageId(resultSet.getInt("messageId"));
                message.setUserId1(resultSet.getInt("userId1"));
                message.setUserId2(resultSet.getInt("userId2"));
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                message.setTimestamp(resultSet.getTimestamp("timestamp",cal));
                message.setMessageContent(resultSet.getString("messageContent"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            close(connection, preparedStatement, resultSet);
        }
        return messages;
    }

    /**
     * Adds a new message to the database
     * @param message the message to add in database
     * @return true on success, false if not
     */
    public Message addMessage(Message message) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.instance().getConnection();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO message (userId1, userId2, timestamp, messageContent) VALUES (?, ?, NOW(), ?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, message.getUserId1());
            preparedStatement.setInt(2, message.getUserId2());
            preparedStatement.setString(3, message.getMessageContent());

            int result = preparedStatement.executeUpdate();

            if (result == 1) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) message.setMessageId(resultSet.getInt(1));
            }
        }catch (SQLIntegrityConstraintViolationException e){
            message = new Message();
            return message;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            close(connection, preparedStatement, resultSet);
        }
        return message;
    }
}
