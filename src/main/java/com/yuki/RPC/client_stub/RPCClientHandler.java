package com.yuki.RPC.client_stub;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Auther: yuki
 * @Date: 2020/2/22 17:17
 * @Description:
 */
public class RPCClientHandler extends ChannelHandlerAdapter {
    /**
     * RPC调用返回的结果
     */
    private Object rpcResult;

    public Object getRpcResult() {
        return rpcResult;
    }

    public void setRpcResult(Object rpcResult) {
        this.rpcResult = rpcResult;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        setRpcResult(msg);
        ctx.close();
    }
}
