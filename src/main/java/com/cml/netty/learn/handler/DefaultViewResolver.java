package com.cml.netty.learn.handler;

import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.activation.MimetypesFileTypeMap;

import com.cml.netty.learn.handler.exception.ResourceNotFundException;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;

public class DefaultViewResolver implements ViewResolver {

	public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
	public static final int HTTP_CACHE_SECONDS = 60;

	private static final String REDIRECT_PREFIX = "redirect:";

	private String basePath;

	public DefaultViewResolver(String basePath) {
		super();
		this.basePath = basePath;
	}

	@Override
	public void resolve(ChannelHandlerContext ctx, FullHttpRequest request, HttpResponseStatus status, ModelAndView model) throws Exception {

		System.out.println("view resolver 处理url:" + model.getView());
		// 重定向
		if (model.getView().startsWith(REDIRECT_PREFIX)) {
			String path = model.getView().substring(REDIRECT_PREFIX.length());
			FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
			response.headers().set(HttpHeaderNames.LOCATION, path);

			// Close the connection as soon as the error message is sent.
			ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
			return;
		}

		RandomAccessFile raf = null;
		try {

			File file = new File(basePath, model.getView());

			if (file.isHidden() || !file.exists()) {
				throw new ResourceNotFundException(model.getView());
			}

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

	/**
	 * Sets the Date header for the HTTP response
	 *
	 * @param response
	 *            HTTP response
	 */
	private static void setDateHeader(FullHttpResponse response) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

		Calendar time = new GregorianCalendar();
		response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));
	}

	/**
	 * Sets the Date and Cache headers for the HTTP Response
	 *
	 * @param response
	 *            HTTP response
	 * @param fileToCache
	 *            file to extract content type
	 */
	private static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

		// Date header
		Calendar time = new GregorianCalendar();
		response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));

		// Add cache headers
		time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
		response.headers().set(HttpHeaderNames.EXPIRES, dateFormatter.format(time.getTime()));
		response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
		response.headers().set(HttpHeaderNames.LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
	}

	/**
	 * Sets the content type header for the HTTP Response
	 *
	 * @param response
	 *            HTTP response
	 * @param file
	 *            file to extract content type
	 */
	private static void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
	}

}
