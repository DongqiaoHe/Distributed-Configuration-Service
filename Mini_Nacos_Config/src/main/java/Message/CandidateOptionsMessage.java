package Message;

import java.util.HashSet;

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
