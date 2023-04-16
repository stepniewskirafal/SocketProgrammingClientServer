package pl.rstepniewski.sockets.domain.message;

public class Message {
    private final static int MAX_LENGTH_OF_MESSAGE = 255;
    private String topic;
    private String content;
    private String recipient;
    private String sender;
    private boolean read;

    public Message(String topic, String content, String recipient, String sender, boolean read) {
        this.topic = topic;
        this.content = content;
        this.recipient = recipient;
        this.sender = sender;
        this.read = read;
    }

    public boolean isRead() {
        return read;
    }
}
