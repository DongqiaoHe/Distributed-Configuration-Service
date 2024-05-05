package Message;

import lombok.Data;

/**
 * @author hedongqiao
 * @date 2024/05/05
 */
@Data
public class PingMessage extends Message{

    /**
     * leader id
     */
    private String leaderId;

    /**
     * @param leaderId
     */
    public PingMessage(String leaderId) {
        this.leaderId = leaderId;
    }

    /**
     * get leader it
     * @return {@link String }
     */
    public String getLeaderId() {
        return leaderId;
    }

    /**
     * get message type
     * @return int
     */
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
