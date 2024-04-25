package Client.Handler;

import Client.Model.Node;
import Message.DataMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DataHandler extends SimpleChannelInboundHandler<DataMessage> {

    Node node;

    public DataHandler(Node node) {
        this.node = node;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataMessage msg) throws Exception {
        node.setUid(msg.getUid());
        System.out.println(msg.getData());

    }
}
