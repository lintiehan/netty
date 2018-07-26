package com.test1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
	private int port;

	public Server(int port) {
		this.port = port;
	}

	public void run() {
		// 用来接收进来的连接
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		// 用来处理已经被接收的连接
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		System.out.println("running port: " + port);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel sc) throws Exception {
							// 自定义处理类
							sc.pipeline().addLast(new HelloServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

			//绑定端口，开始接收进来的连接
			ChannelFuture cf = bootstrap.bind(port).sync();
			//等待服务器socket关闭
			cf.channel().closeFuture().sync();
		} catch (Exception e) {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}

	}
	public static void main(String[] args) {
		new Server(10110).run();
	}
}
