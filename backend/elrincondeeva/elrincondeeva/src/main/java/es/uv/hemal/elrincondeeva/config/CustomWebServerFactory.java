package es.uv.hemal.elrincondeeva.config;

import io.netty.handler.codec.http.HttpHeaderNames;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.http.server.HttpServer;

@Component
public class CustomWebServerFactory implements WebServerFactoryCustomizer<NettyReactiveWebServerFactory> {

    @Override
    public void customize(NettyReactiveWebServerFactory factory) {
        factory.addServerCustomizers(httpServer ->
            httpServer.doOnConnection(conn -> {
                conn.addHandlerLast(new io.netty.channel.ChannelOutboundHandlerAdapter() {
                    @Override
                    public void write(io.netty.channel.ChannelHandlerContext ctx, Object msg, io.netty.channel.ChannelPromise promise) throws Exception {
                        if (msg instanceof io.netty.handler.codec.http.HttpResponse) {
                            ((io.netty.handler.codec.http.HttpResponse) msg).headers().remove(HttpHeaderNames.SERVER);
                        }
                        super.write(ctx, msg, promise);
                    }
                });
            })
        );
    }
}
