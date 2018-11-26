package com.cml.netty.learn.client;

import com.sun.corba.se.internal.CosNaming.BootstrapServer;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @Auther: cml
 * @Date: 2018-11-26 09:29
 * @Description:
 */
public class ClientApp {
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrapServer = new Bootstrap();
        NioEventLoopGroup boss = new NioEventLoopGroup(4);
        bootstrapServer.group(boss).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline ph = socketChannel.pipeline();
                        ph.addLast("framer", new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()));
                        ph.addLast("decoder", new StringDecoder());
                        ph.addLast("encoder", new StringEncoder());
                        ph.addLast("handler", new ClientHandler());
                    }
                });
        Channel channel = bootstrapServer.connect(new InetSocketAddress(8080)).sync().channel();
        channel.writeAndFlush("hello world \r\n");
        channel.closeFuture().sync();
        boss.shutdownGracefully();
        System.out.println("channel  write end======>");
    }
}
