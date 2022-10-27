package codecs;

import domain.FrontendBootstrapMessage;
import domain.FrontendCommandMessage;
import domain.FrontendMessageTypeJ;
import io.netty5.buffer.api.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import io.netty5.handler.codec.MessageToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.ByteBufUtilsJ;

public class PostgresFrontendMessageDecoderJ extends MessageToMessageDecoder<Buffer> {

    private static final Logger logger = LogManager.getLogger(PostgresFrontendMessageDecoderJ.class);

    private boolean startupMessageSeen = false;
    private boolean sslNegotiationMessageSeen = false;

    @Override
    protected void decode(final ChannelHandlerContext ctx, final Buffer msg) throws Exception {
        logger.debug("PostgresFrontendMessageDecoderJ decode");
        if (msg.readableBytes() < 4) {
            return;
        }
        if (!startupMessageSeen) {
            final int _length = msg.readInt();
            final int protocol = msg.readInt();
            switch (protocol) {
                case FrontendBootstrapMessage.SSLRequest.ID -> {
                    logger.debug("SSLRequest");
                    ctx.fireChannelRead(new FrontendBootstrapMessage.SSLRequest());
                    sslNegotiationMessageSeen = true;
                }
                case FrontendBootstrapMessage.Startup.ID -> {
                    logger.debug("Startup");
                    ctx.fireChannelRead(new FrontendBootstrapMessage.Startup(protocol, ByteBufUtilsJ.readCStringMap(msg)));
                    startupMessageSeen = true;
                }
                case FrontendBootstrapMessage.CancelRequest.ID -> {
                    logger.debug("CancelRequest");
                    throw new UnsupportedOperationException("CancelRequest not yet implemented");
                }
                default -> {
                    logger.debug("Unknown message type: {}", protocol);
                    throw new UnsupportedOperationException("Unknown message type: $protocol");
                }
            }
        } else {
            final char messageId = (char) msg.readByte();
            final int _length = msg.readInt();
            logger.debug("Message id: {}", messageId);

            final FrontendMessageTypeJ messageType = FrontendMessageTypeJ.fromId(messageId);
            logger.debug("Message type: {}", messageType);

            if (messageType == FrontendMessageTypeJ.Query) {
                logger.debug("Query");
                final String query = ByteBufUtilsJ.readCString(msg);
                if (query != null && !query.isEmpty()) {
                    ctx.fireChannelRead(new FrontendCommandMessage.Query(query));
                } else {
                    logger.debug("Query is empty");
                }
            }
        }
    }
}
