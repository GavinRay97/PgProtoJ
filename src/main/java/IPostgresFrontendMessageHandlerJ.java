import domain.FrontendBootstrapMessage;
import domain.FrontendCommandMessage;
import io.netty5.channel.ChannelHandlerContext;


public interface IPostgresFrontendMessageHandlerJ {
    
    void handleBind(ChannelHandlerContext ctx, FrontendCommandMessage.Bind msg);

    void handleClose(ChannelHandlerContext ctx, FrontendCommandMessage.Close msg);

    void handleCopyData(ChannelHandlerContext ctx, FrontendCommandMessage.CopyData msg);

    void handleCopyDone(ChannelHandlerContext ctx, FrontendCommandMessage.CopyDone msg);

    void handleCopyFail(ChannelHandlerContext ctx, FrontendCommandMessage.CopyFail msg);

    void handleDescribe(ChannelHandlerContext ctx, FrontendCommandMessage.Describe msg);

    void handleExecute(ChannelHandlerContext ctx, FrontendCommandMessage.Execute msg);

    void handleFlush(ChannelHandlerContext ctx, FrontendCommandMessage.Flush msg);

    void handleFunctionCall(ChannelHandlerContext ctx, FrontendCommandMessage.FunctionCall msg);

    void handleParse(ChannelHandlerContext ctx, FrontendCommandMessage.Parse msg);

    void handlePassword(ChannelHandlerContext ctx, FrontendCommandMessage.Password msg);

    void handleQuery(ChannelHandlerContext ctx, FrontendCommandMessage.Query msg);

    void handleSSLRequest(ChannelHandlerContext ctx, FrontendBootstrapMessage.SSLRequest msg);

    void handleStartup(ChannelHandlerContext ctx, FrontendBootstrapMessage.Startup msg);

    void handleSync(ChannelHandlerContext ctx, FrontendCommandMessage.Sync msg);

    void handleTerminate(ChannelHandlerContext ctx, FrontendCommandMessage.Terminate msg);
}
