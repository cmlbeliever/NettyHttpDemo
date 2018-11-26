package com.cml.netty.learn.handler;

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
	ModelAndView handle(ChannelHandlerContext ctx, FullHttpRequest request, String path);
}
