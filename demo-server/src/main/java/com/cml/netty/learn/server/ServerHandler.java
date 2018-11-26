package com.cml.netty.learn.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Auther: cml
 * @Date: 2018-11-26 09:46
 * @Description:
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println("read server ===>" + s);
        channelHandlerContext.writeAndFlush("response from server " + s);
        channelHandlerContext.writeAndFlush("\r\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("channel active:" + ctx.channel().remoteAddress());
    }
}
