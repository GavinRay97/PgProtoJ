import data.FakeInMemoryDB;
import domain.BackendMessageJ;
import domain.BackendMessageJFieldBuilder;
import domain.CommandType;
import domain.FrontendBootstrapMessage;
import domain.FrontendCommandMessage;
import domain.TransactionStatus;
import io.netty5.buffer.api.Buffer;
import io.netty5.channel.ChannelHandlerContext;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitorAdapter;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExamplePostgresFrontendMessageHandlerJ implements IPostgresFrontendMessageHandlerJ {

    private static final Logger logger = LogManager.getLogger(ExamplePostgresFrontendMessageHandlerJ.class);

    @Override
    public void handleSSLRequest(final ChannelHandlerContext ctx, final FrontendBootstrapMessage.SSLRequest msg) {
        logger.debug("SSLRequest");
        try (final Buffer buf = ctx.bufferAllocator().allocate(1)) {
            buf.writeByte((byte) 'N');
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void handleStartup(final ChannelHandlerContext ctx, final FrontendBootstrapMessage.Startup msg) {
        logger.debug("Startup message seen: {}", msg);
        ctx.write(new BackendMessageJ.AuthenticationOk());
        ctx.write(new BackendMessageJ.BackendKeyData(1, 2));
        ctx.write(new BackendMessageJ.ReadyForQuery(TransactionStatus.IDLE));
        ctx.flush();
    }

    @Override
    public void handleSync(final ChannelHandlerContext ctx, final FrontendCommandMessage.Sync msg) {

    }

    @Override
    public void handleTerminate(final ChannelHandlerContext ctx, final FrontendCommandMessage.Terminate msg) {

    }

    @Override
    public void handleBind(final ChannelHandlerContext ctx, final FrontendCommandMessage.Bind msg) {

    }

    @Override
    public void handleClose(final ChannelHandlerContext ctx, final FrontendCommandMessage.Close msg) {

    }

    @Override
    public void handleCopyData(final ChannelHandlerContext ctx, final FrontendCommandMessage.CopyData msg) {

    }

    @Override
    public void handleCopyDone(final ChannelHandlerContext ctx, final FrontendCommandMessage.CopyDone msg) {

    }

    @Override
    public void handleCopyFail(final ChannelHandlerContext ctx, final FrontendCommandMessage.CopyFail msg) {

    }

    @Override
    public void handleDescribe(final ChannelHandlerContext ctx, final FrontendCommandMessage.Describe msg) {

    }

    @Override
    public void handleExecute(final ChannelHandlerContext ctx, final FrontendCommandMessage.Execute msg) {

    }

    @Override
    public void handleFlush(final ChannelHandlerContext ctx, final FrontendCommandMessage.Flush msg) {

    }

    @Override
    public void handleFunctionCall(final ChannelHandlerContext ctx, final FrontendCommandMessage.FunctionCall msg) {

    }

    @Override
    public void handleParse(final ChannelHandlerContext ctx, final FrontendCommandMessage.Parse msg) {

    }

    @Override
    public void handlePassword(final ChannelHandlerContext ctx, final FrontendCommandMessage.Password msg) {

    }

    @Override
    public void handleQuery(final ChannelHandlerContext ctx, final FrontendCommandMessage.Query msg) {
        logger.debug("Query message seen: {}", msg);
        try {
            final Statement stmt = CCJSqlParserUtil.parse(msg.query());
            logger.debug("Parsed statement: {}", stmt);

            if (stmt instanceof Select select) {
                final List<String> tableList = new TablesNamesFinder().getTableList(select);
                final String tableName = tableList.get(0);
                logger.debug("Table name: {}", tableName);

                final Map<String, String> columnMap = new HashMap<>();
                ((PlainSelect) select.getSelectBody()).getSelectItems().forEach(item -> {
                    item.accept(new SelectItemVisitorAdapter() {
                        @Override
                        public void visit(final SelectExpressionItem item) {
                            final Column column = (Column) item.getExpression();
                            columnMap.put(item.getAlias() != null
                                            ? item.getAlias().getName()
                                            : column.getColumnName(),
                                    column.getColumnName());
                        }
                    });
                });

                final List<Map<String, Object>> rows = FakeInMemoryDB.TABLES.get(tableName).stream()
                        .map(row -> {
                            final Map<String, Object> rowMap = new HashMap<>();
                            row.forEach((key, value) -> rowMap.put(columnMap.get(key), value));
                            return rowMap;
                        })
                        .toList();

                final BackendMessageJ.RowDescription rowDescription = new BackendMessageJ.RowDescription(
                        columnMap.keySet().stream().map(expression -> BackendMessageJFieldBuilder.builder()
                                .name(expression)
                                .tableOid(0)
                                .tableColumn(0)
                                .dataTypeOid(25)
                                .dataTypeModifier(-1)
                                .dataTypeSize(-1)
                                .formatCode(0)
                                .build()).toList());
                ctx.write(rowDescription);

                rows.forEach(row -> {
                    final BackendMessageJ.DataRow dataRow = new BackendMessageJ.DataRow(row.values());
                    ctx.write(dataRow);
                });

                ctx.write(new BackendMessageJ.CommandComplete(1, CommandType.SELECT));
                ctx.write(new BackendMessageJ.ReadyForQuery(TransactionStatus.IDLE));
                ctx.flush();
            } else {
                throw new IllegalStateException("Unexpected value: " + stmt);
            }
        } catch (final JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }
}

