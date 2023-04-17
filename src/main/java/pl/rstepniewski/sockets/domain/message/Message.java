package pl.rstepniewski.sockets.domain.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private String topic;
    private String content;
    private String recipient;
    private String sender;

    public Message(@JsonProperty("topic") String topic, @JsonProperty("content") String content, @JsonProperty("recipient") String recipient, @JsonProperty("sender") String sender) {
        this.topic = topic;
        this.content = content;
        this.recipient = recipient;
        this.sender = sender;
    }

    public Message() {
    }

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }
}
