package com.cml.netty.learn.form;

import java.io.File;

import com.cml.netty.learn.handler.HandlerRequestAdapter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public class LoginHandler implements HandlerRequestAdapter {

	@Override
	public boolean handle(ChannelHandlerContext ctx, FullHttpRequest request, File target) {
		return false;
	}

}
