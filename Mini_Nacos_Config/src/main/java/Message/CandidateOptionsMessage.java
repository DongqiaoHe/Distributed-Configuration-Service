package Message;

import java.util.HashSet;

/**
 * CandidateOptionsMessage is a message that contains the candidate options for the dataset
 * @author hedongqiao
 * @date 2024/05/05
 */
public class CandidateOptionsMessage extends Message{

    HashSet<String> ids;

    public HashSet<String> getIds() {
        return ids;
    }

    public void setIds(HashSet<String> ids) {
        this.ids = ids;
    }

    @Override
    public int getMessageType() {
        return CandidateOptionsMessage;
    }
}
