package com.cml.netty.learn.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * 静态文件处理类
 * 
 * @author cml
 *
 */
public class StaticResourceHandlerRequestAdapter implements HandlerRequestAdapter {

	private String defaultView;

	public StaticResourceHandlerRequestAdapter(String defaultView) {
		super();
		this.defaultView = defaultView;
	}

	public StaticResourceHandlerRequestAdapter() {
	}

	@Override
	public ModelAndView handle(ChannelHandlerContext ctx, FullHttpRequest request, String path) {
		System.out.println("path===>" + path);
		return new ModelAndView(null == defaultView ? path : defaultView);
	}

}
