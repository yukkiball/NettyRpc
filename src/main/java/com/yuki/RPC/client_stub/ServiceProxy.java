package com.yuki.RPC.client_stub;

import com.yuki.RPC.common.ClassInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;



/**
 * @Auther: yuki
 * @Date: 2020/2/22 17:12
 * @Description:
 */
public class ServiceProxy {

    public static Object create(Class clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                //构造调用信息
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassName(clazz.getName());
                classInfo.setMethodName(method.getName());
                classInfo.setParamTypes(method.getParameterTypes());
                classInfo.setParams(args);

                //使用netty发送调用信息给服务提供方
                NioEventLoopGroup group = new NioEventLoopGroup();
                Bootstrap bootstrap = new Bootstrap();
                RPCClientHandler rpcClientHandler = new RPCClientHandler();
                try {
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .option(ChannelOption.SO_KEEPALIVE, true)
                            .option(ChannelOption.TCP_NODELAY, true)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ch.pipeline().addLast(new ObjectEncoder());
                                    //反序列化对象时指定类解析器，null表示使用默认的类加载器
                                    ch.pipeline().addLast(new ObjectDecoder(1024 * 64, ClassResolvers.cacheDisabled(null)));
                                    ch.pipeline().addLast(rpcClientHandler);

                                }
                            });
                    //connect是异步的，但调用其future的sync则是同步等待连接成功
                    ChannelFuture future = bootstrap.connect("127.0.0.1", 9000).sync();
                    //同步等待调用信息发送成功
                    future.channel().writeAndFlush(classInfo).sync();
                    //同步等待RPCClientHandler的channelRead被触发后（意味着收到了调用结果）
                    future.channel().closeFuture().sync();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }

                //返回调用结果
                return rpcClientHandler.getRpcResult();
            }
        });
    }
}
