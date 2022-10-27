import codecs.PostgresBackendMessageEncoderJ;
import codecs.PostgresFrontendMessageDecoderJ;
import io.netty5.bootstrap.ServerBootstrap;
import io.netty5.channel.ChannelInitializer;
import io.netty5.channel.EventLoopGroup;
import io.netty5.channel.MultithreadEventLoopGroup;
import io.netty5.channel.nio.NioHandler;
import io.netty5.channel.socket.SocketChannel;
import io.netty5.channel.socket.nio.NioServerSocketChannel;
import io.netty5.handler.logging.LogLevel;
import io.netty5.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

public class Netty5PostgresWireServerJ {
    private static final String HOST = System.getProperty("host", "0.0.0.0");
    private static final int PORT = Integer.parseInt(System.getProperty("port", "5400"));

    public static void main(final String[] args) {
        final EventLoopGroup bossGroup = new MultithreadEventLoopGroup(1, NioHandler.newFactory());
        final EventLoopGroup workerGroup = new MultithreadEventLoopGroup(NioHandler.newFactory());
        try {
            final ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.TRACE))
                    .childHandler(new ServerInitializer());
            b.bind(HOST, PORT).get().closeFuture().sync();
        } catch (final ExecutionException e) {
            throw new RuntimeException(e);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    static class ServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(final SocketChannel ch) throws Exception {
            ch.pipeline()
                    .addLast(new LoggingHandler(LogLevel.TRACE))
                    .addLast(new PostgresBackendMessageEncoderJ())
                    .addLast(new PostgresFrontendMessageDecoderJ())
                    .addLast(
                            new DelegatingPostgresFrontendMessageHandlerJ(
                                    new ExamplePostgresFrontendMessageHandlerJ()
                            )
                    );
        }
    }
}
