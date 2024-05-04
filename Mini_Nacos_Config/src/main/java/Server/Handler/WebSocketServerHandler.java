package Server.Handler;

import Message.*;
import Server.ChannelService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.HashSet;

import static Const.MessageConst.UNCOMMITTED;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Message> {

    //Netty提供的组件，按照<ChannelID,Channel>的格式缓存连接

    private static ChannelService channelService = ChannelService.getChannelService();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) throws Exception {
        if(msg instanceof PingMessage && ((PingMessage) msg).getLeaderId().equals(ctx.channel().id().asShortText())){
            //心跳机制
            System.out.println("Ping from "+ctx.channel().id().asShortText());
        }

        if(msg instanceof IncrementalDataMessage){
            //leader收到消息之后，给全部的followers发送ack。
            IncrementalDataMessage incrementalDataMessage = (IncrementalDataMessage) msg;
            if(incrementalDataMessage.getState() == UNCOMMITTED){
                channelService.getChannelGroup().forEach(channel -> {
                    channel.writeAndFlush(incrementalDataMessage);
                });
            }
        }

        if(msg instanceof AckDataMessage){
            AckDataMessage ackDataMessage = (AckDataMessage) msg;
            ackDataMessage.setChannelNum(channelService.getChannelGroup().size());
            //收到ack之后，给leader发送ack。
            channelService.getLeaderChannel().writeAndFlush(msg);
        }

        if(msg instanceof CommitDataMessage){

            //收到ack之后，给leader发送ack。
            channelService.getChannelGroup().writeAndFlush(msg);
        }

        if(msg instanceof FullDataMessage){
            FullDataMessage fullDataMessage = (FullDataMessage) msg;
            channelService.getChannel(fullDataMessage.getReceivedId()).writeAndFlush(new FullDataMessage(fullDataMessage.getReceivedId(), fullDataMessage.getDataset()));
        }

        if(msg instanceof RemoveDataMessage){
            RemoveDataMessage removeDataMessage = (RemoveDataMessage) msg;
            channelService.getChannelGroup().writeAndFlush(removeDataMessage);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        String leaderMessage = "";
        String leaderId = "";
        String nodeMessage = "";
        channelService.getChannelGroup().add(ctx.channel());
        channelService.addChannel(ctx.channel());
        nodeMessage = "Your ID= "+ctx.channel().id().asShortText();
        ctx.channel().writeAndFlush(new DataMessage(ctx.channel().id().asShortText(), nodeMessage));
        System.out.println("Node connected：ID= "+ctx.channel().id().asShortText());
        if(channelService.getLeaderId().equals("")){
            leaderId = ctx.channel().id().asShortText();
            leaderMessage = "New Leader ID: " + leaderId;
            System.out.println("New Leader ID: "+leaderId);
            channelService.setLeaderId(leaderId);
            channelService.getChannelGroup().writeAndFlush(new ElectionMessage(leaderId, leaderMessage));
        }else{
            leaderId = channelService.getLeaderId();
            leaderMessage = "Current Leader ID: "+ channelService.getLeaderId();
            ctx.channel().writeAndFlush(new ElectionMessage(leaderId, leaderMessage));
            channelService.getLeaderChannel().writeAndFlush(new AddNodeMessage(ctx.channel().id().asShortText()));
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelService.getChannelGroup().remove(ctx.channel());
        channelService.closeChannel(ctx.channel());
        System.out.println("Disconnected Node ID= "+ctx.channel().id().asShortText());
        channelService.getChannelGroup().writeAndFlush(new DataMessage(ctx.channel().id().asShortText(), " is removed"));
        if(channelService.getLeaderId().equals(ctx.channel().id().asShortText())){
            //leader is disconnected
            System.out.println("Leader ID: "+ctx.channel().id().asShortText() + " is discounted! ");
            channelService.setLeaderId(null);

            //election new leader
            CandidateOptionsMessage candidateOptionsMessage = new CandidateOptionsMessage();
            candidateOptionsMessage.setIds(new HashSet<>(channelService.getAllChannel().keySet()));
            channelService.getChannelGroup().writeAndFlush(candidateOptionsMessage);
        }
    }
}
