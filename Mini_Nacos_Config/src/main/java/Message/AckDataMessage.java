package Message;

/**
 * @author hedongqiao
 * @date 2024/05/05
 */
public class AckDataMessage extends Message{

    String key;

    String senderId;

    int channelNum;

    /**
     * @param key
     * @param senderId
     */
    public AckDataMessage(String key, String senderId) {
        this.key = key;
        this.senderId = senderId;
    }


    public int getChannelNum() {
        return channelNum;
    }

    /**
     * update the number of channels
     * @param channelNum
     */
    public void setChannelNum(int channelNum) {
        this.channelNum = channelNum;
    }

    public String getKey() {
        return key;
    }

    /**
     * @return {@link String } sender id
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * @return int type of message
     */
    @Override
    public int getMessageType() {
        return AckDataMessage;
    }

    @Override
    public String toString() {
        return "AckDataMessage{" +
                "key='" + key + '\'' +
                ", senderId='" + senderId + '\'' +
                '}';
    }
}
