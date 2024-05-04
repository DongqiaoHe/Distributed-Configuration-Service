package Message;

import Client.Model.DataEntry;

import java.util.concurrent.ConcurrentHashMap;

public class FullDataMessage extends Message{

    private ConcurrentHashMap<String, DataEntry> dataset;

    private String receivedId;

    public FullDataMessage(String receivedId, ConcurrentHashMap<String, DataEntry> dataset) {
        this.dataset = dataset;
        this.receivedId =  receivedId;
    }

    public String getReceivedId() {
        return receivedId;
    }

    public ConcurrentHashMap<String, DataEntry> getDataset() {
        return dataset;
    }

    @Override
    public int getMessageType() {
        return FullDataMessage;
    }
}
