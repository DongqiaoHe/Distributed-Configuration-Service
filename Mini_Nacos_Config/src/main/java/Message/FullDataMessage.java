package Message;

import Client.Model.DataEntry;

import java.util.concurrent.ConcurrentHashMap;

/**
 * FullDataMessage is a message that contains all the data in the dataset
 * @author hedongqiao
 * @date 2024/05/05
 */
public class FullDataMessage extends Message{

    private ConcurrentHashMap<String, DataEntry> dataset;

    private String receivedId;

    public FullDataMessage(String receivedId, ConcurrentHashMap<String, DataEntry> dataset) {
        this.dataset = dataset;
        this.receivedId =  receivedId;
    }

    /**
     * @return {@link String }
     */
    public String getReceivedId() {
        return receivedId;
    }

    /**
     * get the dataset map
     * @return {@link ConcurrentHashMap }<{@link String }, {@link DataEntry }>
     */
    public ConcurrentHashMap<String, DataEntry> getDataset() {
        return dataset;
    }

    /**
     * get message type
     * @return int
     */
    @Override
    public int getMessageType() {
        return FullDataMessage;
    }
}
