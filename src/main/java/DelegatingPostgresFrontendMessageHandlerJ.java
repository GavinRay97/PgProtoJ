import domain.FrontendBootstrapMessage;
import domain.FrontendCommandMessage;
import domain.IFrontendMessage;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DelegatingPostgresFrontendMessageHandlerJ extends SimpleChannelInboundHandler<IFrontendMessage> {

    private static final Logger logger = LogManager.getLogger(DelegatingPostgresFrontendMessageHandlerJ.class);

    private final IPostgresFrontendMessageHandlerJ handler;

    DelegatingPostgresFrontendMessageHandlerJ(final IPostgresFrontendMessageHandlerJ handler) {
        this.handler = handler;
    }

    @Override
    protected void messageReceived(final ChannelHandlerContext ctx, final IFrontendMessage msg) throws Exception {
        logger.debug("DelegatingPostgresFrontendMessageHandlerJ messageReceived");

        // Dispatch messages to delegate interface handler
        switch (msg) {
            case FrontendBootstrapMessage.Startup startup -> {
                logger.debug("Startup");
                handler.handleStartup(ctx, startup);
            }
            case FrontendBootstrapMessage.SSLRequest sslRequest -> {
                logger.debug("SSLRequest");
                handler.handleSSLRequest(ctx, sslRequest);
            }
            case FrontendCommandMessage.Query query -> {
                logger.debug("Query");
                handler.handleQuery(ctx, query);
            }
            default -> {
                logger.debug("Unknown message type: {}", msg);
                throw new UnsupportedOperationException("Unknown message type: $msg");
            }
        }
    }
}
