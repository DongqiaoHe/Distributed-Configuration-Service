package Message;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class ElectionMessage extends Message {

    private String leaderId;
    private String message;

    public ElectionMessage(String leaderId, String message) {
        this.leaderId = leaderId;
        this.message = message;
    }


    public String getLeaderId() {
        return leaderId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int getMessageType() {
        return ElectionMessage;
    }
}
