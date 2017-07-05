package com.cml.netty.learn.handler;

import java.io.File;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class IndexHandlerRequestAdapter implements HandlerRequestAdapter {


	@Override
	public boolean handle(ChannelHandlerContext ctx, FullHttpRequest request, File target) {
		// TODO Auto-generated method stub
		return false;
	}

}
