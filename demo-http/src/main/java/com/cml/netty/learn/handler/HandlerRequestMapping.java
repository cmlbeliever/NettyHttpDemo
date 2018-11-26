package com.cml.netty.learn.handler;

public interface HandlerRequestMapping {
	HandlerRequestAdapter mapping(String path);
}
