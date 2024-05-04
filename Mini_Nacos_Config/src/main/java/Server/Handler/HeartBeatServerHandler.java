package Server.Handler;


import Message.PingMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

import static io.netty.handler.timeout.IdleState.READER_IDLE;

public class HeartBeatServerHandler extends SimpleChannelInboundHandler<PingMessage> {

    int readIdleTimes = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingMessage s) throws Exception {
        System.err.println(ctx.channel().id().asShortText()+ " is active");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        if(READER_IDLE.equals(event.state())) {
            readIdleTimes++;
        }
        System.out.println("Missing heartbeat from leader: " + ctx.channel().id().asShortText());
        if (readIdleTimes > 3) {
            System.out.println("Missing leader heartbeat over 3 times, election new leader");
            ctx.channel().writeAndFlush("Channel close");
            ctx.channel().close();
        }
    }

}
