package rs.ac.bg.fon.chatbot.db.domain;

public class Message {

    private String senderID;
    private Object text;
    private MessageType type;

    public Message(String senderID, Object text, MessageType type) {
        this.senderID = senderID;
        this.text = text;
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Object getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
