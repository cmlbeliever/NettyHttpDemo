package com.cml.netty.learn.handler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

/**
 * 所有的错误页面处理url
 * 
 * @author cml
 *
 */
public class ErrorHandlerRequestAdapter implements HandlerRequestAdapter {

	private File defaultFile;
	private HttpResponseStatus status;

	public ErrorHandlerRequestAdapter(File defaultFile, HttpResponseStatus status) {
		super();
		this.defaultFile = defaultFile;
		this.status = status;
	}

	@Override
	public boolean handle(ChannelHandlerContext ctx, FullHttpRequest request, File target) {

		File file = (null == defaultFile ? target : defaultFile);

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
			Long fileLength = raf.length();
			ByteBuffer buffer = ByteBuffer.allocate(fileLength.intValue());
			raf.getChannel().read(buffer);
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, Unpooled.wrappedBuffer(buffer.array()));
			
			response.headers().set(HttpHeaderNames.CONTENT_ENCODING, "utf-8");
			if (file.getName().endsWith("html")) {
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html");
			} else if (file.getName().endsWith("css")) {
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/css");
			} else if (file.getName().endsWith("js")) {
				response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/javascript");
			}
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());

			if (HttpUtil.isKeepAlive(request)) {
				response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
			}

			// 输出
			ctx.writeAndFlush(response);

			return true;
		} catch (Exception ignore) {
			return false;
		} finally {
			if (null != raf) {
				try {
					raf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
