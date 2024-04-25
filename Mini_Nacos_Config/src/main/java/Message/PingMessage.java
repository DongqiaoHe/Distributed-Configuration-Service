package Message;

import lombok.Data;

@Data
public class PingMessage extends Message{

    private String leaderId;

    public PingMessage(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
