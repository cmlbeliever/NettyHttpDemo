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
 * 静态文件处理类
 * 
 * @author cml
 *
 */
public class StaticResourceHandlerRequestAdapter implements HandlerRequestAdapter {

	private File defaultFile;

	public StaticResourceHandlerRequestAdapter(File defaultFile) {
		super();
		this.defaultFile = defaultFile;
	}

	public StaticResourceHandlerRequestAdapter() {
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
			FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
					Unpooled.wrappedBuffer(buffer.array()));
			String accept = request.headers().get(HttpHeaderNames.ACCEPT);
			System.out.println(accept);
			response.headers().set(HttpHeaderNames.CONTENT_ENCODING, "utf-8");
			// response.headers().set(HttpHeaderNames.ACCEPT_CHARSET, "zh");

			// SimpleDateFormat dateFormatter = new
			// SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
			// dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));
			// Calendar time = new GregorianCalendar();
			// response.headers().set(HttpHeaderNames.LAST_MODIFIED,
			// dateFormatter.format(time.getTime()));
			// setDateHeader(response);

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
