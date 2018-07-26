package com;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	private final ByteBuf byteBuf;

	public ClientHandler() {
		byte[] bytes = "i miss u".getBytes();
		byteBuf = Unpooled.buffer(bytes.length);
		byteBuf.writeBytes(bytes);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(byteBuf);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// ��ȡ����˷�����������
		ByteBuf buf = (ByteBuf) msg;
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		String message = new String(bytes, "UTF-8");
		System.out.println("�͑����յ�����Ϣ��:"+message);
	}
}