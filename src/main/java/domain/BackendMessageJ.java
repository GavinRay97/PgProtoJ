package domain;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.util.Collection;

public sealed interface BackendMessageJ {
    char getId();

    record AuthenticationOk() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationKerberosV5() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationCleartextPassword() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationSCMCredential() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationGSS() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationSSPI() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationGSSContinue() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationSASL() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationSASLContinue() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record AuthenticationSASLFinal() implements BackendMessageJ {
        static final char ID = 'R';

        @Override
        public char getId() {
            return ID;
        }
    }

    record BackendKeyData(int processId, int secretKey) implements BackendMessageJ {
        static final char ID = 'K';

        @Override
        public char getId() {
            return ID;
        }
    }

    record BindComplete() implements BackendMessageJ {
        static final char ID = '2';

        @Override
        public char getId() {
            return ID;
        }
    }

    record CloseComplete() implements BackendMessageJ {
        static final char ID = '3';

        @Override
        public char getId() {
            return ID;
        }
    }

    record CommandComplete(int affectedRows, CommandType command) implements BackendMessageJ {
        static final char ID = 'C';

        @Override
        public char getId() {
            return ID;
        }
    }

    @SuppressWarnings("java:S6218")
    record CopyData(byte[] data) implements BackendMessageJ {
        static final char ID = 'd';

        @Override
        public char getId() {
            return ID;
        }
    }

    record CopyDone() implements BackendMessageJ {
        static final char ID = 'c';

        @Override
        public char getId() {
            return ID;
        }
    }

    record CopyInResponse() implements BackendMessageJ {
        static final char ID = 'G';

        @Override
        public char getId() {
            return ID;
        }
    }

    record CopyOutResponse() implements BackendMessageJ {
        static final char ID = 'H';

        @Override
        public char getId() {
            return ID;
        }
    }

    record CopyBothResponse() implements BackendMessageJ {
        static final char ID = 'W';

        @Override
        public char getId() {
            return ID;
        }
    }

    record DataRow(Collection<Object> columns) implements BackendMessageJ {
        static final char ID = 'D';

        @Override
        public char getId() {
            return ID;
        }
    }

    record EmptyQueryResponse() implements BackendMessageJ {
        static final char ID = 'I';

        @Override
        public char getId() {
            return ID;
        }
    }

    record ErrorResponse(String errorMessage) implements BackendMessageJ {
        static final char ID = 'E';

        @Override
        public char getId() {
            return ID;
        }
    }

    record FunctionCallResponse(String data) implements BackendMessageJ {
        static final char ID = 'V';

        @Override
        public char getId() {
            return ID;
        }
    }

    record NegotiateProtocolVersion(int data) implements BackendMessageJ {
        static final char ID = 'v';

        @Override
        public char getId() {
            return ID;
        }
    }

    record NoData() implements BackendMessageJ {
        static final char ID = 'n';

        @Override
        public char getId() {
            return ID;
        }
    }

    record NoticeResponse(String data) implements BackendMessageJ {
        static final char ID = 'N';

        @Override
        public char getId() {
            return ID;
        }
    }

    record NotificationResponse(String data) implements BackendMessageJ {
        static final char ID = 'A';

        @Override
        public char getId() {
            return ID;
        }
    }

    record ParameterDescription(Collection<String> parameterNames) implements BackendMessageJ {
        static final char ID = 't';

        @Override
        public char getId() {
            return ID;
        }
    }

    record ParameterStatus(String data) implements BackendMessageJ {
        static final char ID = 'S';

        @Override
        public char getId() {
            return ID;
        }
    }

    record ParseComplete() implements BackendMessageJ {
        static final char ID = '1';

        @Override
        public char getId() {
            return ID;
        }
    }

    record PortalSuspended() implements BackendMessageJ {
        static final char ID = 's';

        @Override
        public char getId() {
            return ID;
        }
    }

    record ReadyForQuery(TransactionStatus state) implements BackendMessageJ {
        static final char ID = 'Z';

        @Override
        public char getId() {
            return ID;
        }
    }

    @RecordBuilder
    record Field(
            String name,
            int dataTypeOid,
            int dataTypeSize,
            int dataTypeModifier,
            int tableOid,
            int tableColumn,
            int formatCode) {
    }

    record RowDescription(Collection<Field> fields) implements BackendMessageJ {
        static final char ID = 'T';

        @Override
        public char getId() {
            return ID;
        }
    }


}
