package domain;

import io.netty5.buffer.api.Buffer;
import utils.ByteBufUtilsJ;

import java.util.Map;

public sealed interface FrontendBootstrapMessage extends IFrontendMessage {

    static FrontendBootstrapMessage from(final int id, final Buffer msg) {
        return switch (id) {
            case Startup.ID -> new Startup(id, ByteBufUtilsJ.readCStringMap(msg));
            case SSLRequest.ID -> new SSLRequest();
            case CancelRequest.ID -> new CancelRequest();
            default -> throw new IllegalArgumentException("Unknown id: " + id);
        };
    }

    int getId();

    record Startup(int version, Map<String, String> params) implements FrontendBootstrapMessage {
        public static final int ID = 196608; // First Hextet: 3 (version), Second Hextet: 0

        @Override
        public int getId() {
            return ID;
        }
    }

    record SSLRequest() implements FrontendBootstrapMessage {
        public static final int ID = 80877103; // First Hextet: 1234, Second Hextet: 5679

        @Override
        public int getId() {
            return ID;
        }
    }

    record CancelRequest() implements FrontendBootstrapMessage {
        public static final int ID = 80877102; // First Hextet: 1234, Second Hextet: 5678

        @Override
        public int getId() {
            return ID;
        }
    }
}
