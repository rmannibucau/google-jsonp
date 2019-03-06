package com.github.rmannibucau.google.json;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.json.JsonNumber;
import javax.json.stream.JsonParsingException;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;

class JsonParserImpl extends JsonParser {
    private final JsonFactory factory;
    private final javax.json.stream.JsonParser delegate;

    private JsonToken current = null;

    JsonParserImpl(final JsonFactory factory,
                          final javax.json.stream.JsonParser delegate) {
        this.factory = factory;
        this.delegate = delegate;
    }

    @Override
    public JsonFactory getFactory() {
        return factory;
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public JsonToken nextToken() {
        if (!delegate.hasNext()) {
            return current = null;
        }
        try {
            return current = mapToken(delegate.next());
        } catch (final ArrayIndexOutOfBoundsException | JsonParsingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public JsonToken getCurrentToken() {
        return current;
    }

    @Override
    public String getCurrentName() {
        try {
            return delegate.getString();
        } catch (final IllegalStateException ise) {
            throw new IllegalArgumentException(ise);
        }
    }

    @Override
    public JsonParser skipChildren() {
        if (current == JsonToken.START_ARRAY) {
            delegate.skipArray();
            current = JsonToken.END_ARRAY;
        } else if (current == JsonToken.START_OBJECT) {
            delegate.skipObject();
            current = JsonToken.END_OBJECT;
        }
        return this;
    }

    @Override
    public String getText() {
        return delegate.getString();
    }

    @Override
    public byte getByteValue() {
        return (byte) delegate.getInt();
    }

    @Override
    public short getShortValue() {
        return (short) delegate.getInt();
    }

    @Override
    public int getIntValue() {
        return delegate.getInt();
    }

    @Override
    public float getFloatValue() {
        return (float) JsonNumber.class.cast(delegate.getValue()).doubleValue();
    }

    @Override
    public long getLongValue() {
        return delegate.getLong();
    }

    @Override
    public double getDoubleValue() {
        return JsonNumber.class.cast(delegate.getValue()).doubleValue();
    }

    @Override
    public BigInteger getBigIntegerValue() {
        return JsonNumber.class.cast(delegate.getValue()).bigIntegerValue();
    }

    @Override
    public BigDecimal getDecimalValue() {
        return delegate.getBigDecimal();
    }

    private JsonToken mapToken(final javax.json.stream.JsonParser.Event current) {
        switch (current) {
            case START_ARRAY:
                return JsonToken.START_ARRAY;
            case START_OBJECT:
                return JsonToken.START_OBJECT;
            case KEY_NAME:
                return JsonToken.FIELD_NAME;
            case VALUE_STRING:
                return JsonToken.VALUE_STRING;
            case VALUE_NUMBER:
                if (delegate.isIntegralNumber()) {
                    return JsonToken.VALUE_NUMBER_INT;
                }
                return JsonToken.VALUE_NUMBER_FLOAT;
            case VALUE_TRUE:
                return JsonToken.VALUE_TRUE;
            case VALUE_FALSE:
                return JsonToken.VALUE_FALSE;
            case VALUE_NULL:
                return JsonToken.VALUE_NULL;
            case END_OBJECT:
                return JsonToken.END_OBJECT;
            case END_ARRAY:
                return JsonToken.END_ARRAY;
            default:
                throw new IllegalArgumentException("unknown event: " + current);
        }
    }
}
