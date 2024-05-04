package Message;

public class RemoveDataMessage extends Message{

    private String key;

    public RemoveDataMessage(String key){
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int getMessageType() {
        return RemoveDataMessage;
    }
}
