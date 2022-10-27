package domain;

public sealed interface IFrontendMessage permits FrontendCommandMessage, FrontendBootstrapMessage {
}
