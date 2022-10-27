package codecs;

import domain.BackendMessageJ;
import io.netty5.buffer.api.Buffer;
import io.netty5.channel.ChannelHandler;
import io.netty5.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ChannelHandler.Sharable
public class PostgresBackendMessageEncoderJ extends io.netty5.handler.codec.MessageToByteEncoderForBuffer<BackendMessageJ> {

    private static final Logger logger = LogManager.getLogger(PostgresBackendMessageEncoderJ.class);

    private static String convertToPostgresString(final Object value) {
        return switch (value) {
            case null -> "NULL";
            case String string -> "'" + string + "'";
            case Integer integer -> integer.toString();
            case Long longValue -> longValue.toString();
            case Double doubleValue -> doubleValue.toString();
            case Boolean booleanValue -> booleanValue ? "TRUE" : "FALSE";
            default -> throw new IllegalArgumentException("Unsupported type: " + value.getClass());
        };
    }

    @Override
    protected Buffer allocateBuffer(final ChannelHandlerContext ctx, final BackendMessageJ msg) throws Exception {
        return ctx.bufferAllocator().allocate(1024);
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final BackendMessageJ msg, final Buffer out) throws Exception {
        logger.debug("PostgresBackendMessageEncoderJ encode");

        final PostgresWireMessageBuilder written = switch (msg) {
            case BackendMessageJ.AuthenticationOk ignored -> {
                logger.debug("AuthenticationOk");
                yield new PostgresWireMessageBuilder(msg.getId(), out)
                        .writeInt32(0, "success")
                        .finalizeMessage();
            }
            case BackendMessageJ.BackendKeyData keyData -> {
                logger.debug("BackendKeyData");
                yield new PostgresWireMessageBuilder(msg.getId(), out)
                        .writeInt32(keyData.processId(), "pid")
                        .writeInt32(keyData.secretKey(), "key")
                        .finalizeMessage();
            }
            case BackendMessageJ.ReadyForQuery readyForQuery -> {
                logger.debug("ReadyForQuery");
                yield new PostgresWireMessageBuilder(msg.getId(), out)
                        .writeByte((byte) readyForQuery.state().getId(), "state")
                        .finalizeMessage();
            }
            case BackendMessageJ.RowDescription rowDescription -> {
                logger.debug("RowDescription");
                final PostgresWireMessageBuilder builder = new PostgresWireMessageBuilder(msg.getId(), out);
                builder.writeInt16((short) rowDescription.fields().size(), "numFields");
                rowDescription.fields().forEach(field ->
                        builder.writeCString(field.name(), "name")
                                .writeInt32(field.tableOid(), "tableOid")
                                .writeInt16((short) field.tableColumn(), "tableColumn")
                                .writeInt32(field.dataTypeOid(), "dataTypeOid")
                                .writeInt16((short) field.dataTypeSize(), "dataTypeSize")
                                .writeInt32(field.dataTypeModifier(), "dataTypeModifier")
                                .writeInt16((short) field.formatCode(), "format"));
                yield builder.finalizeMessage();
            }
            case BackendMessageJ.DataRow dataRow -> {
                logger.debug("DataRow");
                final PostgresWireMessageBuilder builder = new PostgresWireMessageBuilder(msg.getId(), out);
                builder.writeInt16((short) dataRow.columns().size(), "numColumns");
                dataRow.columns().forEach(column -> {
                    final var colValueAsString = convertToPostgresString(column);
                    builder.writeInt32(colValueAsString.length(), "columnLength");
                    builder.writeBytes(colValueAsString.getBytes(), "columnValue");
                });
                yield builder.finalizeMessage();
            }
            case BackendMessageJ.CommandComplete commandComplete -> {
                logger.debug("CommandComplete");
                final String command = switch (commandComplete.command()) {
                    case INSERT -> "INSERT 0";
                    case UPDATE -> "UPDATE";
                    case DELETE -> "DELETE";
                    case SELECT -> "SELECT";
                    case COPY -> "COPY";
                    case MOVE -> "MOVE";
                    case FETCH -> "FETCH";
                };
                yield new PostgresWireMessageBuilder(msg.getId(), out)
                        .writeCString(command + " " + commandComplete.affectedRows(), "command")
                        .finalizeMessage();
            }

            default -> throw new IllegalStateException("Unexpected value: " + msg);
        };

        logger.debug("PostgresBackendMessageEncoderJ encode done");
        logger.debug("Buf: {}", written.getBuf().toString(io.netty.util.CharsetUtil.UTF_8));
    }
}

