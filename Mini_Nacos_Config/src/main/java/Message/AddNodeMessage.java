package Message;

/**
 * message that new node join in the group
 * @author hedongqiao
 * @date 2024/05/05
 */
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
