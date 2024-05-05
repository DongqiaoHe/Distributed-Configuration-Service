package Message;

import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Data;
import lombok.ToString;

/**
 * Election result message
 * @author hedongqiao
 * @date 2024/05/05
 */
@Data
@ToString(callSuper = true)
public class ElectionMessage extends Message {

    /**
     * new leader id
     */
    private String leaderId;

    /**
     * message
     */
    private String message;

    public ElectionMessage(String leaderId, String message) {
        this.leaderId = leaderId;
        this.message = message;
    }

    /**
     * get the leader id
     * @return {@link String }
     */
    public String getLeaderId() {
        return leaderId;
    }

    /**
     * @return {@link String }
     */
    public String getMessage() {
        return message;
    }

    /**
     * get message type
     * @return int
     */
    @Override
    public int getMessageType() {
        return ElectionMessage;
    }
}
