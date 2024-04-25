package Client;

import Client.Handler.ChatClientHandler;
import Client.Handler.DataHandler;
import Client.Handler.ElectionHandler;
import Client.Handler.PingHandler;
import Client.Model.Node;
import Protocal.MessageCodecSharable;
import Protocal.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class NettyClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Node node = new Node();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(new MessageCodecSharable());
                    //加入自己的业务处理handler
                    ch.pipeline().addLast(new ChatClientHandler());
                    ch.pipeline().addLast(new DataHandler(node));
                    ch.pipeline().addLast(new ElectionHandler(node));
                    ch.pipeline().addLast(new PingHandler());
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}


