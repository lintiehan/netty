package com.test1;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class HelloServerHandler extends ChannelInboundHandlerAdapter {
	// �յ������r�{��
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		try {
			ByteBuf in = (ByteBuf) msg;
			System.out.println(in.toString(CharsetUtil.UTF_8));
		} finally {
			// �ח��յ��Ĕ���
			ReferenceCountUtil.release(msg);
		}
	}

	// ��netty����IO������ߴ������ڴ���ʱ��ʱ�׳��쳣ʱ����
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// �������쳣�͹ر�����
		cause.printStackTrace();
		ctx.close();
	}
}
