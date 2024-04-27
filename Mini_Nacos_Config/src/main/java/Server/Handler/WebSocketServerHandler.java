package Server.Handler;

import Message.ElectionMessage;
import Message.DataMessage;
import Message.PingMessage;
import Message.Message;
import Server.ChannelService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.time.LocalDateTime;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Message> {

    //Netty提供的组件，按照<ChannelID,Channel>的格式缓存连接
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static ChannelService channelService = new ChannelService();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        //System.out.println("服务器收到消息："+msg.getMessage());
        //String strResp = "服务器时间" + LocalDateTime.now() + ",客户端"+ctx.channel().id()+"，发送消息：" + msg.getMessage();
//        final TextWebSocketFrame response = new TextWebSocketFrame(strResp);
//        ctx.channel().writeAndFlush(response);
//        channelGroup.writeAndFlush(response);
//        channelService.broadCast(strResp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("发生异常："+cause.getMessage());
        channelService.closeChannel(ctx.channel());
        channelGroup.remove(ctx.channel());
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String leaderMessage = "";
        String leaderId = "";
        String nodeMessage = "";
        channelGroup.add(ctx.channel());
        channelService.addChannel(ctx.channel());
        nodeMessage = "Your ID= "+ctx.channel().id().asShortText();
        ctx.channel().writeAndFlush(new DataMessage(ctx.channel().id().asShortText(), nodeMessage));
        System.out.println("Node connected：ID= "+ctx.channel().id().asShortText());
        if(channelService.getLeaderId().equals("")){
            leaderId = ctx.channel().id().asShortText();
            leaderMessage = "New Leader ID: " + leaderId;
            System.out.println("New Leader ID: "+leaderId);
            channelService.setLeaderId(leaderId);
            channelGroup.writeAndFlush(new ElectionMessage(leaderId, leaderMessage));
        }else{
            leaderId = channelService.getLeaderId();
            leaderMessage = "Current Leader ID: "+ channelService.getLeaderId();
            ctx.channel().writeAndFlush(new ElectionMessage(leaderId, leaderMessage));
        }


        new Thread(()->{
            while(true){
                try {
                    Thread.sleep(5000);
                    if(ctx.channel().id().asShortText().equals(channelService.getLeaderId())){
                        channelGroup.iterator().forEachRemaining(channel -> {
                            if(!channel.id().asShortText().equals(channelService.getLeaderId())){
                                channel.writeAndFlush(new PingMessage(channelService.getLeaderId()));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.remove(ctx.channel());
        channelService.closeChannel(ctx.channel());
        System.out.println("断开连接：ID= "+ctx.channel().id().asShortText());

        if(channelService.getLeaderId().equals(ctx.channel().id().asShortText())){
            System.out.println("Leader ID: "+ctx.channel().id().asShortText() + " is discounted! ");
            String newLeaderId = channelService.allocateLeader();
            channelService.setLeaderId(newLeaderId);
            if(!newLeaderId.equals("")){
                System.out.println("New Leader ID: "+newLeaderId);
                channelGroup.writeAndFlush(new ElectionMessage(newLeaderId, "New Leader ID: " + newLeaderId));
            }
        }
    }
}
