package Client.Handler;

import Message.ElectionMessage;
import Message.PingMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class PingHandler  extends SimpleChannelInboundHandler<PingMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingMessage msg) throws Exception {
        System.out.println(msg.getLeaderId() + " is alive!");
    }
}
