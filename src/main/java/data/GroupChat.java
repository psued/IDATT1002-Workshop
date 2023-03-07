package data;

import java.util.ArrayList;

/**
 * Class for the GroupChat object as saved in database
 */
public class GroupChat {
  private int groupChatId;
  private String groupChatName;
  private ArrayList<Message> messageList;
  private ArrayList<User> userList;

  public GroupChat() {
  }

  public GroupChat(int groupChatId, String groupChatName) {
    this.groupChatId = groupChatId;
    this.groupChatName = groupChatName;
    messageList = new ArrayList<Message>();
    userList = new ArrayList<User>();
  }

  public int getGroupChatId() {
    return groupChatId;
  }

  public String getGroupChatName() {
    return groupChatName;
  }

  public ArrayList<Message> getMessageList() {
    return messageList;
  }

  public ArrayList<User> getUserList() {
    return userList;
  }

  public void setGroupChatId(int groupChatId) {
    this.groupChatId = groupChatId;
  }

  public void setGroupChatName(String groupChatName) {
    this.groupChatName = groupChatName;
  }

  public void setMessageList(ArrayList<Message> messageList) {
    this.messageList = messageList;
  }

  public void setUserList(ArrayList<User> userList) {
    this.userList = userList;
  }

}
