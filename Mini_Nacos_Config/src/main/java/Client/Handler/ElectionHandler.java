package Client.Handler;

import Client.Model.Node;
import Message.ElectionMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ElectionHandler extends SimpleChannelInboundHandler<ElectionMessage> {

    Node node;

    public ElectionHandler(Node node) {
        this.node = node;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ElectionMessage electionMessage) throws Exception {
        String leaderId = electionMessage.getLeaderId();
        node.setLeaderId(leaderId);
        System.out.println(electionMessage.getMessage());
        if(node.getUid().equals(node.getLeaderId())){
            System.out.println("You are elected as the leader!");
        }else{
            System.out.println("You am not the leader!");
        }
    }
}
