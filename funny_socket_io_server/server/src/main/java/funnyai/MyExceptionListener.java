/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funnyai;

import com.corundumstudio.socketio.listener.ExceptionListenerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author happyli
 */
public class MyExceptionListener extends ExceptionListenerAdapter{
    @Override
    public boolean exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        System.out.println(e.getMessage());
        ctx.close();
        
        return true;
    }
}
