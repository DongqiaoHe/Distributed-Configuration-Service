package Message;

/**
 * Message that contains the key to commit the data in the dataset
 * @author hedongqiao
 * @date 2024/05/05
 */
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
