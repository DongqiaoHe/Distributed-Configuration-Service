package Server;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelService {

    private ConcurrentHashMap<String, Channel> allChannel = new ConcurrentHashMap<>();


    private static ChannelService channelService = new ChannelService();

    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private ChannelService(){}

    public static ChannelService getChannelService(){
        return channelService;
    }

    private String leaderId = "";

    public ConcurrentHashMap<String, Channel> getAllChannel() {
        return allChannel;
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    /**
     * add channel to allChannel
     * @param channel
     * @return {@link Boolean }
     */
    public Boolean addChannel(Channel channel){
        Boolean added = false;
        String channelId = channel.id().asShortText();
        if(!allChannel.containsKey(channelId)){
            allChannel.putIfAbsent(channelId,channel);
            added = true;
        }
        return added;

    }

    /**
     * delete channel from allChannel
     * @param channel
     * @return {@link Boolean }
     */
    public Boolean closeChannel(Channel channel){
        String channelId = channel.id().asShortText();
        Channel c = allChannel.remove(channelId);
        if(null ==c){
            return true;
        }
        return false;
    }

    /**
     * set leader id
     * @param leaderId
     */
    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    /**
     * get leader id
     * @return {@link String }
     */
    public String getLeaderId() {
        return leaderId;
    }

    /**
     * return leader channel
     * @return {@link Channel }
     */
    public Channel getLeaderChannel(){
        return allChannel.get(leaderId);
    }

    /**
     * return according to the channel short id
     *
     * @param id channel id
     * @return {@link Channel }
     */
    public Channel getChannel(String id){
        return allChannel.get(id);
    }
}
