package Message;

public class AckDataMessage extends Message{

    String key;

    String senderId;

    int channelNum;

    public AckDataMessage(String key, String senderId) {
        this.key = key;
        this.senderId = senderId;
    }

    public int getChannelNum() {
        return channelNum;
    }

    public void setChannelNum(int channelNum) {
        this.channelNum = channelNum;
    }

    public String getKey() {
        return key;
    }

    public String getSenderId() {
        return senderId;
    }

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
