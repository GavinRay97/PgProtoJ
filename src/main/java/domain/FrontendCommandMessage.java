package domain;

import java.util.Map;


public sealed interface FrontendCommandMessage extends IFrontendMessage {
    char getId();


    record Bind(String name, Map<String, String> params) implements FrontendCommandMessage {
        static final char ID = 'B';

        @Override
        public char getId() {
            return ID;
        }
    }

    record Close() implements FrontendCommandMessage {
        static final char ID = 'C';

        @Override
        public char getId() {
            return ID;
        }
    }

    @SuppressWarnings("java:S6218")
    record CopyData(byte[] data) implements FrontendCommandMessage {
        static final char ID = 'd';

        @Override
        public char getId() {
            return ID;
        }
    }

    record CopyDone() implements FrontendCommandMessage {
        static final char ID = 'c';

        @Override
        public char getId() {
            return ID;
        }
    }


    record CopyFail() implements FrontendCommandMessage {
        @Override
        public char getId() {
            return 'f';
        }
    }

    record Describe(String name) implements FrontendCommandMessage {
        static final char ID = 'D';

        @Override
        public char getId() {
            return ID;
        }
    }

    record Execute(String name, Map<String, String> params) implements FrontendCommandMessage {
        static final char ID = 'E';

        @Override
        public char getId() {
            return ID;
        }
    }

    record Flush() implements FrontendCommandMessage {
        static final char ID = 'H';

        @Override
        public char getId() {
            return ID;
        }
    }

    record FunctionCall(String name, Map<String, String> params) implements FrontendCommandMessage {
        static final char ID = 'F';

        @Override
        public char getId() {
            return ID;
        }
    }

    record Parse(String name, Map<String, String> params) implements FrontendCommandMessage {
        static final char ID = 'P';

        @Override
        public char getId() {
            return ID;
        }
    }

    record Password(String password) implements FrontendCommandMessage {
        static final char ID = 'p';

        @Override
        public char getId() {
            return ID;
        }
    }

    record Query(String query) implements FrontendCommandMessage {
        static final char ID = 'Q';

        @Override
        public char getId() {
            return ID;
        }
    }

    record Sync() implements FrontendCommandMessage {
        static final char ID = 'S';

        @Override
        public char getId() {
            return ID;
        }
    }

    record Terminate() implements FrontendCommandMessage {
        static final char ID = 'X';

        @Override
        public char getId() {
            return ID;
        }
    }

}
