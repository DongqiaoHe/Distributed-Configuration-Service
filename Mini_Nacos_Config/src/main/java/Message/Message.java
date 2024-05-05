package Message;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hedongqiao
 * @date 2024/05/05
 */
@Data
public abstract class Message implements Serializable {

    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
    public static Class<? extends Message> getMessageClass(int messageType) {
        return messageClasses.get(messageType);
    }

    private int sequenceId;

    private int messageType;

    public abstract int getMessageType();

    public static final int ElectionMessage = 0;

    public static final int DataMessage = 1;

    public static final int PingMessage = 2;

    public static final int CandidateOptionsMessage = 3;

    public static final int IncrementalDataMessage = 4;

    public static final int AckDataMessage = 5;

    public static final int CommitDataMessage = 6;

    public static final int AddNodeMessage = 7;

    public static final int FullDataMessage = 8;

    public static final int RemoveDataMessage = 9;

    /**
     * 请求类型 byte 值
     */
    public static final int RPC_MESSAGE_TYPE_REQUEST = 101;
    /**
     * 响应类型 byte 值
     */
    public static final int  RPC_MESSAGE_TYPE_RESPONSE = 102;

    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();

    static {
        messageClasses.put(ElectionMessage, ElectionMessage.class);
        messageClasses.put(DataMessage, DataMessage.class);
        messageClasses.put(PingMessage, PingMessage.class);
        messageClasses.put(CandidateOptionsMessage, CandidateOptionsMessage.class);
        messageClasses.put(IncrementalDataMessage, IncrementalDataMessage.class);
        messageClasses.put(AckDataMessage, AckDataMessage.class);
        messageClasses.put(CommitDataMessage, CommitDataMessage.class);
        messageClasses.put(AddNodeMessage, AddNodeMessage.class);
        messageClasses.put(FullDataMessage, FullDataMessage.class);
        messageClasses.put(RemoveDataMessage, RemoveDataMessage.class);
    }

    public int getSequenceId() {
        return sequenceId;
    }
}