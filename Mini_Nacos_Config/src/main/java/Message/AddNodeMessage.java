package Message;

public class AddNodeMessage extends Message{

    String newId;

    public AddNodeMessage(String newId) {
        this.newId = newId;
    }

    public String getNewId() {
        return newId;
    }

    @Override
    public int getMessageType() {
        return AddNodeMessage;
    }
}
