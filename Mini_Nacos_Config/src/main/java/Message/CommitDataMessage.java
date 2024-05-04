package Message;

public class CommitDataMessage extends Message{
    String Key;

    public CommitDataMessage(String key) {
        this.Key = key;
    }

    public String getKey() {
        return Key;
    }

    @Override
    public int getMessageType() {
        return CommitDataMessage;
    }

    @Override
    public String toString() {
        return "CommitDataMessage{" +
                "Key='" + Key + '\'' +
                '}';
    }
}
