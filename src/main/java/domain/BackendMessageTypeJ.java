package domain;

public enum BackendMessageTypeJ {
    AuthenticationOk('R'),
    AuthenticationKerberosV5('R'),
    AuthenticationCleartextPassword('R'),
    AuthenticationMD5Password('R'),
    AuthenticationSCMCredential('R'),
    AuthenticationGSS('R'),
    AuthenticationSSPI('R'),
    AuthenticationGSSContinue('R'),
    AuthenticationSASL('R'),
    AuthenticationSASLContinue('R'),
    AuthenticationSASLFinal('R'),
    BackendKeyData('K'),
    BindComplete('2'),
    CloseComplete('3'),
    CommandComplete('4'),
    CopyData('d'),
    CopyDone('c'),
    CopyInResponse('G'),
    CopyOutResponse('H'),
    CopyBothResponse('W'),
    DataRow('D'),
    EmptyQueryResponse('I'),
    ErrorResponse('E'),
    FunctionCallResponse('V'),
    NegotiateProtocolVersion('v'),
    NoData('n'),
    NoticeResponse('N'),
    NotificationResponse('A'),
    ParameterDescription('t'),
    ParameterStatus('S'),
    ParseComplete('1'),
    PortalSuspended('s'),
    ReadyForQuery('Z'),
    RowDescription('T');

    private final char id;

    BackendMessageTypeJ(final char id) {
        this.id = id;
    }

    public static BackendMessageTypeJ fromId(final char id) {
        for (final BackendMessageTypeJ value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum constant domain.BackendMessageTypeJ." + id);
    }

    public char getId() {
        return id;
    }
}
