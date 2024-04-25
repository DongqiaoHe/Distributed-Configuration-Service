package Message;

import java.util.Map;

public class DataMessage extends Message{


    private String uid;
    private String data;

    public DataMessage(String uid, String s) {
        this.uid = uid;
        this.data = s;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int getMessageType() {
        return DataMessage;
    }

    @Override
    public String toString() {
        return "DataMessage{" +
                "uid='" + uid + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
