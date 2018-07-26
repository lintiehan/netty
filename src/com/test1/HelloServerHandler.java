package com.test1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {
	// 收到數據時調用
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf in = (ByteBuf) msg;
			System.out.println(in.toString(CharsetUtil.UTF_8));
		} finally {
			// 抛棄收到的數據
			ReferenceCountUtil.release(msg);
		}
	}

	// 当netty由于IO错误或者处理器在处理时间时抛出异常时调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 当出现异常就关闭连接
		cause.printStackTrace();
		ctx.close();
	}
}
