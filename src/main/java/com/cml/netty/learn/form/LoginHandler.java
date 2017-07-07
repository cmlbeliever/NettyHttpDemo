package com.cml.netty.learn.form;

import java.util.List;
import java.util.Map;

import com.cml.netty.learn.handler.HandlerRequestAdapter;
import com.cml.netty.learn.handler.ModelAndView;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;

public class LoginHandler implements HandlerRequestAdapter {

	@Override
	public ModelAndView handle(ChannelHandlerContext ctx, FullHttpRequest request, String path) {
		System.out.println("method:" + request.method());
		if (request.method() != HttpMethod.POST) {
			System.out.println("un support not post!!");
			return new ModelAndView("404.html");
		}

		String content = new String(request.content().copy().array());

		QueryStringDecoder decoderQuery = new QueryStringDecoder("uri?" + content);
		Map<String, List<String>> uriAttributes = decoderQuery.parameters();

		String username = uriAttributes.get("email").isEmpty() ? null : uriAttributes.get("email").get(0);
		String password = uriAttributes.get("password").isEmpty() ? null : uriAttributes.get("password").get(0);

		System.out.println("用户" + username + ",使用密码：" + password + ",登录成功！");

		return new ModelAndView("redirect:/LoginResult.html");
	}

}
