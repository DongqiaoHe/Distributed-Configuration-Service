package Message;

import static Const.MessageConst.UNCOMMITTED;

public class IncrementalDataMessage extends Message{
    String Key;
    String Value;
    int state;

    int ack;

    public IncrementalDataMessage(String key, String value, int state) {
        this.Key = key;
        this.Value = value;
        this.state = state;
        this.ack = 0;

    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getStrState() {
        return state == UNCOMMITTED ? "UNCOMMITTED" : "COMMITTED";
    }

    public int getState() {
        return state;
    }

    @Override
    public int getMessageType() {
        return IncrementalDataMessage;
    }

    public int getAck() {
        return ack;
    }

    @Override
    public String toString() {
        return "SetDataMessage{" +
                "Key='" + Key + '\'' +
                ", Value='" + Value + '\'' +
                ", state=" + state +
                ", ack=" + ack +
                '}';
    }
}
