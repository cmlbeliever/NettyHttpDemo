package com.cml.netty.learn;

import com.cml.netty.learn.form.LoginHandler;
import com.cml.netty.learn.handler.DefaultHandlerRequestMapping;
import com.cml.netty.learn.handler.HandlerRequestMapping;
import com.cml.netty.learn.handler.StaticResourceHandlerRequestAdapter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * 服务器入口
 * 
 * @author admin
 *
 */
public class NettyHttpServer {
	private static final String BASE_FILE = "src/main/webapp";

	public void start(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					pipeline.addLast(new HttpServerCodec());
					pipeline.addLast(new HttpObjectAggregator(65536));
					pipeline.addLast(new ChunkedWriteHandler());
					pipeline.addLast(new HttpServerHandler(BASE_FILE, registerMappings()));
				}
			}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			ChannelFuture f = b.bind(port).sync();

			System.out.println("server started.....");

			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	static HandlerRequestMapping registerMappings() {
		DefaultHandlerRequestMapping mapping = new DefaultHandlerRequestMapping();
		mapping.register("/", new StaticResourceHandlerRequestAdapter("Demo.html"));
		mapping.register("/login", new LoginHandler());
		mapping.register("/js/.*\\.js", new StaticResourceHandlerRequestAdapter());
		mapping.register("/css/.*\\.css", new StaticResourceHandlerRequestAdapter());
		mapping.register(".*\\.html", new StaticResourceHandlerRequestAdapter());
		mapping.register(String.valueOf(HttpResponseStatus.NOT_FOUND), new StaticResourceHandlerRequestAdapter("404.html"));
		return mapping;
	}

	public static void main(String[] args) throws Exception {
		NettyHttpServer server = new NettyHttpServer();
		server.start(8080);
	}
}
