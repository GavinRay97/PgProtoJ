package codecs;

import io.netty5.buffer.api.Buffer;
import io.netty5.util.CharsetUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresWireMessageBuilder {
    private final Buffer buf;
    private final List<WireMessageField> fields;
    private final int lengthFieldPos;
    private int length;

    PostgresWireMessageBuilder(final char messageId, final Buffer buf) {
        this.buf = buf;

        this.length = 0;
        this.fields = new ArrayList<>();

        // Write the message's identifier character byte to the buffer
        // but don't increment the length, since we don't count it in the message size
        buf.writeByte((byte) messageId);
        fields.add(new WireMessageField(WireMessageField.Type.CHAR, messageId, 1, length));

        // Store a reference to the position of the length field in the buffer
        this.lengthFieldPos = buf.writerOffset();
        this.writeInt32(0, "Length"); // length placeholder
    }

    PostgresWireMessageBuilder finalizeMessage() {
        buf.setInt(lengthFieldPos, length);
        return this;
    }

    public int getLength() {
        return length;
    }

    Buffer getBuf() {
        return buf;
    }

    public PostgresWireMessageBuilder writeInt16(final short value) {
        buf.writeShort(value);
        length += 2;
        fields.add(new WireMessageField(WireMessageField.Type.INT16, value, 2, length));
        return this;
    }

    PostgresWireMessageBuilder writeInt16(final short value, final String name) {
        buf.writeShort(value);
        length += 2;
        fields.add(new WireMessageField(WireMessageField.Type.INT16, Optional.of(name), value, 2, length));
        return this;
    }

    private PostgresWireMessageBuilder writeInt32(final int value) {
        buf.writeInt(value);
        length += 4;
        fields.add(new WireMessageField(WireMessageField.Type.INT32, value, 4, length));
        return this;
    }

    PostgresWireMessageBuilder writeInt32(final int value, final String name) {
        buf.writeInt(value);
        length += 4;
        fields.add(new WireMessageField(WireMessageField.Type.INT32, Optional.of(name), value, 4, length));
        return this;
    }

    public PostgresWireMessageBuilder writeInt64(final long value) {
        buf.writeLong(value);
        length += 8;
        fields.add(new WireMessageField(WireMessageField.Type.INT64, value, 8, length));
        return this;
    }

    public PostgresWireMessageBuilder writeInt64(final long value, final String name) {
        buf.writeLong(value);
        length += 8;
        fields.add(new WireMessageField(WireMessageField.Type.INT64, Optional.of(name), value, 8, length));
        return this;
    }

    public PostgresWireMessageBuilder writeInt64(final double value) {
        buf.writeDouble(value);
        length += 8;
        fields.add(new WireMessageField(WireMessageField.Type.INT64, value, 8, length));
        return this;
    }

    private PostgresWireMessageBuilder writeChar(final char value) {
        buf.writeByte((byte) value);
        length += 1;
        fields.add(new WireMessageField(WireMessageField.Type.CHAR, value, 1, length));
        return this;
    }

    public PostgresWireMessageBuilder writeChar(final char value, final String name) {
        buf.writeByte((byte) value);
        length += 1;
        fields.add(new WireMessageField(WireMessageField.Type.CHAR, Optional.of(name), value, 1, length));
        return this;
    }

    public PostgresWireMessageBuilder writeString(final String value) {
        buf.writeCharSequence(value, CharsetUtil.UTF_8);
        length += value.length();
        fields.add(new WireMessageField(WireMessageField.Type.STRING, value, value.length(), length));
        return this;
    }

    public PostgresWireMessageBuilder writeString(final String value, final String name) {
        buf.writeCharSequence(value, CharsetUtil.UTF_8);
        length += value.length();
        fields.add(new WireMessageField(WireMessageField.Type.STRING, Optional.of(name), value, value.length(), length));
        return this;
    }

    public PostgresWireMessageBuilder writeCString(final String value) {
        buf.writeBytes(value.getBytes());
        buf.writeByte((byte) 0);
        length += value.length() + 1;
        fields.add(new WireMessageField(WireMessageField.Type.CSTRING, value, value.length() + 1, length));
        return this;
    }

    PostgresWireMessageBuilder writeCString(final String value, final String name) {
        buf.writeBytes(value.getBytes());
        buf.writeByte((byte) 0);
        length += value.length() + 1;
        fields.add(new WireMessageField(WireMessageField.Type.CSTRING, Optional.of(name), value, value.length() + 1, length));
        return this;
    }

    public PostgresWireMessageBuilder writeByte(final byte value) {
        buf.writeByte(value);
        length += 1;
        fields.add(new WireMessageField(WireMessageField.Type.BYTE, value, 1, length));
        return this;
    }

    PostgresWireMessageBuilder writeByte(final byte value, final String name) {
        buf.writeByte(value);
        length += 1;
        fields.add(new WireMessageField(WireMessageField.Type.BYTE, Optional.of(name), value, 1, length));
        return this;
    }

    public PostgresWireMessageBuilder writeBytes(final byte[] value) {
        buf.writeBytes(value);
        length += value.length;
        fields.add(new WireMessageField(WireMessageField.Type.BYTE, value, value.length, length));
        return this;
    }

    PostgresWireMessageBuilder writeBytes(final byte[] value, final String name) {
        buf.writeBytes(value);
        length += value.length;
        fields.add(new WireMessageField(WireMessageField.Type.BYTE, Optional.of(name), value, value.length, length));
        return this;
    }

    record WireMessageField(Type type, Optional<String> name, Object value, int size, int pos) {

        WireMessageField(final Type type, final Object value, final int size, final int pos) {
            this(type, Optional.empty(), value, size, pos);
        }

        enum Type {
            INT16,
            INT32,
            INT64,
            CHAR,
            STRING,
            CSTRING,
            BYTE
        }
    }


}
