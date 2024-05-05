package Message;

/**
 * @author hedongqiao
 * @date 2024/05/05
 */
public class RemoveDataMessage extends Message{

    /**
     * key to remove in the message
     */
    private String key;

    /**
     * @param key
     */
    public RemoveDataMessage(String key){
        this.key = key;
    }

    /**
     * get removed key
     * @return {@link String }
     */
    public String getKey() {
        return key;
    }

    @Override
    public int getMessageType() {
        return RemoveDataMessage;
    }
}
