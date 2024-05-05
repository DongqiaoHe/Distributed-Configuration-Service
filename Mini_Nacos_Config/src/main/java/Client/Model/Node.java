package Client.Model;


import java.util.concurrent.ConcurrentHashMap;

import static Const.MessageConst.*;

/**
 * @author hedongqiao
 * @date 2024/05/05
 */
public class Node {


    private String uid;

    private String leaderId;

    private ConcurrentHashMap<String, DataEntry> dataset;


    public Node() {
        dataset = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, DataEntry> getDataset() {
        return dataset;
    }

    public DataEntry getDataEntry(String key) {
        return dataset.get(key);
    }

    public void addData(String key, DataEntry dataEntry) {
        dataset.put(key, dataEntry);
    }

    public void commitData(String key) {
        dataset.get(key).setState(COMMITTED);
    }

    public void setDataset(ConcurrentHashMap<String, DataEntry> dataset) {
        this.dataset = dataset;
    }

    public void addAck(String key) {
        dataset.get(key).addAck();
    }


    public void removeData(String key) {
        DataEntry res = dataset.remove(key);
        if (res != null) {
            System.out.println("Data key: " + key + " || value:" + res.getValue() + " is removed!");
        }
    }



    public int getDataAck(String key) {
        return dataset.get(key).getAck();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }
}
