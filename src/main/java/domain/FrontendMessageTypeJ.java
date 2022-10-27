package domain;

public enum FrontendMessageTypeJ {
    Bind('B'),
    Close('C'),
    CopyData('d'),
    CopyDone('c'),
    CopyFail('f'),
    Describe('D'),
    Execute('E'),
    Flush('H'),
    FunctionCall('F'),
    Parse('P'),
    Password('p'),
    Query('Q'),
    Sync('S'),
    Terminate('X');

    private final char id;

    FrontendMessageTypeJ(final char id) {
        this.id = id;
    }

    public static FrontendMessageTypeJ fromId(final char id) {
        for (final FrontendMessageTypeJ messageType : values()) {
            if (messageType.getId() == id) {
                return messageType;
            }
        }
        throw new IllegalArgumentException("Unknown message type id: " + id);
    }

    public char getId() {
        return id;
    }
}
