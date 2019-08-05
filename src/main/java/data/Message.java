package data;

import java.sql.Timestamp;

/**
 * Class for the Message object as saved in database.
 */
public class Message {
    private int messageId;
    private int userId1;
    private int userId2;
    private Timestamp timestamp;
    private String messageContent;
    private int groupChatId;


    public Message(){}

    public Message(int userId1, int userId2, int messageId, String messageContent, Timestamp timestamp) {
        this.messageId = messageId;
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.timestamp = timestamp;
        this.messageContent = messageContent;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getUserId1() {
        return userId1;
    }

    public void setUserId1(int userId1) {
        this.userId1 = userId1;
    }

    public int getUserId2() {
        return userId2;
    }

    public void setUserId2(int userId2) {
        this.userId2 = userId2;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public void setGroupChatId(int groupChatId){
        this.groupChatId = groupChatId;
    }

    public int getGroupChatId(){
        return groupChatId;
    }
}
