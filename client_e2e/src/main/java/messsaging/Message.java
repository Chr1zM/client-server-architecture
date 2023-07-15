package messsaging;

import java.io.Serializable;

public class Message implements Serializable {
    byte[] content;
    String from;
    String to;

    public Message(String from, String to, byte[] content) {
        this.from = from;
        this.to = to;
        this.content = content;
    }

    public String getFrom() {
        return this.from;
    }

    public String getTo() {
        return this.to;
    }

    public byte[] getContent() {
        return this.content;
    }
}
