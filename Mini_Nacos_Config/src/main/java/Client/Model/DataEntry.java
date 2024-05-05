package Client.Model;

import static Const.MessageConst.UNCOMMITTED;

/**
 * @author hedongqiao
 * @date 2024/05/05
 */
public class DataEntry {
    String key;

    String value;

    int state;

    int ack;

    public DataEntry(String key, String value, int state) {
        this.key = key;
        this.value = value;
        this.state = state;
        this.ack = 0;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        if(this.state == UNCOMMITTED){
            return "Data is not committed yet!";
        }
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getState() {
        return state == UNCOMMITTED ? "UNCOMMITTED" : "COMMITTED";
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getAck() {
        return ack;
    }

    public void addAck() {
        ack++;
    }
}
