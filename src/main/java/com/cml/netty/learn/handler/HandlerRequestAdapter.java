package com.cml.netty.learn.handler;

import java.io.File;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface HandlerRequestAdapter {
	/**
	 * 处理网络请求
	 * 
	 * @param ctx
	 * @param request
	 * @return true 处理成功
	 */
	boolean handle(ChannelHandlerContext ctx, FullHttpRequest request,File target);
}
