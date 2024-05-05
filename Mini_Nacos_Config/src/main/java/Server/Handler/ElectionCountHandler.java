package Server.Handler;

import Message.ElectionMessage;
import Server.ChannelService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Synchronized;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ElectionCountHandler extends SimpleChannelInboundHandler<ElectionMessage> {

    /**
     * key: leaderId
     * value: count
     */
    ConcurrentHashMap<String, Integer> electionCount = new ConcurrentHashMap<>();

    /**
     *  ChannelService to get channels
     */
    private static ChannelService channelService = ChannelService.getChannelService();

    static AtomicInteger votes = new AtomicInteger(0);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ElectionMessage msg) throws Exception {
        String leaderId = msg.getLeaderId();
        System.out.println("Received vote for: "+leaderId + " from "+ctx.channel().id().asShortText() + " ,because: "+msg.getMessage());
        if(electionCount.containsKey(leaderId)){
            electionCount.put(leaderId, electionCount.get(leaderId)+1);
        }else{
            electionCount.put(leaderId, 1);
        }
        votes.addAndGet(1);
        synchronized(votes){
            if(votes.get() == channelService.getAllChannel().size()){
                int maxVotes = 0;
                String newLeader = "";
                for(String key : electionCount.keySet()){
                    if(electionCount.get(key) > maxVotes){
                        maxVotes = electionCount.get(key);
                        newLeader = key;
                    }
                }
                channelService.setLeaderId(newLeader);
                System.out.println("New Leader: "+newLeader);
                channelService.getChannelGroup().writeAndFlush(new ElectionMessage(newLeader, "New Leader: "+newLeader));
                electionCount.clear();
                votes = new AtomicInteger(0);
            }
        }

    }

}
