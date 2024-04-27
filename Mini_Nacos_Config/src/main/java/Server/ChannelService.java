package Server;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

public class ChannelService {

    private ConcurrentHashMap<String, Channel> allChannel = new ConcurrentHashMap<>();

    private String leaderId = "";

    public Boolean addChannel(Channel channel){
        Boolean added = false;
        String channelId = channel.id().asShortText();
        if(!allChannel.containsKey(channelId)){
            allChannel.putIfAbsent(channelId,channel);
            added = true;
        }
        return added;

    }

    public void broadCast(String message){
        for (Channel channel : allChannel.values()) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
        }
    }

    public Boolean closeChannel(Channel channel){
        String channelId = channel.id().asShortText();
        Channel c = allChannel.remove(channelId);
        if(null ==c){
            return true;
        }
        return false;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public String allocateLeader() {
        String leaderId = "";
        for (String id : allChannel.keySet()) {
            if (id.compareTo(leaderId) != 0) {
                leaderId = id;
            }

        }
        return leaderId;
    }
}
