package com.cml.netty.learn.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

public interface ViewResolver {
	void resolve(ChannelHandlerContext ctx,FullHttpRequest request,HttpResponseStatus status,  ModelAndView model) throws Exception;
}
