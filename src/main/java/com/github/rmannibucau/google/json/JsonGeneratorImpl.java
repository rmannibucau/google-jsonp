package com.github.rmannibucau.google.json;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.function.Function;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;

class JsonGeneratorImpl extends JsonGenerator {
    private final JsonFactory factory;
    private final javax.json.stream.JsonGenerator providedGenerator;
    private final Function<Boolean, javax.json.stream.JsonGenerator> generatorFactory;

    private javax.json.stream.JsonGenerator delegate;
    private boolean pretty;

    // to manage the diff between write(key, value) and writeKey()writeValue() APIs
    private final LinkedList<String> keys = new LinkedList<>();
    private final LinkedList<Boolean> containerIsArray = new LinkedList<>();

    JsonGeneratorImpl(final JsonFactory factory,
                      final Function<Boolean, javax.json.stream.JsonGenerator> generatorFactory) {
        this.factory = factory;
        this.generatorFactory = generatorFactory;
        this.providedGenerator = delegate;
    }

    @Override
    public void enablePrettyPrint() throws IOException {
        pretty = true;
    }

    @Override
    public JsonFactory getFactory() {
        return factory;
    }

    @Override
    public void flush() {
        delegate.flush();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public void writeStartArray() {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.writeStartArray();
        } else if (containerIsArray.getLast()) {
            delegate.writeStartArray();
        } else {
            delegate.writeStartArray(keys.removeLast());
        }
        containerIsArray.add(true);
    }

    @Override
    public void writeEndArray() {
        delegate.writeEnd();
        containerIsArray.removeLast();
    }

    @Override
    public void writeStartObject() {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.writeStartObject();
        } else if (containerIsArray.getLast()) {
            delegate.writeStartObject();
        } else {
            delegate.writeStartObject(keys.removeLast());
        }
        containerIsArray.add(false);
    }

    @Override
    public void writeEndObject() {
        delegate.writeEnd();
        containerIsArray.removeLast();
    }

    @Override
    public void writeFieldName(final String name) {
        keys.add(name);
    }

    @Override
    public void writeNull() {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.writeNull();
        } else if (containerIsArray.getLast()) {
            delegate.writeNull();
        } else {
            delegate.writeNull(keys.removeLast());
        }
    }

    @Override
    public void writeString(final String value) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(value);
        } else if (containerIsArray.getLast()) {
            delegate.write(value);
        } else {
            delegate.write(keys.removeLast(), value);
        }
    }

    @Override
    public void writeBoolean(final boolean state) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(state);
        } else if (containerIsArray.getLast()) {
            delegate.write(state);
        } else {
            delegate.write(keys.removeLast(), state);
        }
    }

    @Override
    public void writeNumber(final int v) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(v);
        } else if (containerIsArray.getLast()) {
            delegate.write(v);
        } else {
            delegate.write(keys.removeLast(), v);
        }
    }

    @Override
    public void writeNumber(final long v) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(v);
        } else if (containerIsArray.getLast()) {
            delegate.write(v);
        } else {
            delegate.write(keys.removeLast(), v);
        }
    }

    @Override
    public void writeNumber(final BigInteger v) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(v);
        } else if (containerIsArray.getLast()) {
            delegate.write(v);
        } else {
            delegate.write(keys.removeLast(), v);
        }
    }

    @Override
    public void writeNumber(final float v) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(v);
        } else if (containerIsArray.getLast()) {
            delegate.write(v);
        } else {
            delegate.write(keys.removeLast(), v);
        }
    }

    @Override
    public void writeNumber(final double v) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(v);
        } else if (containerIsArray.getLast()) {
            delegate.write(v);
        } else {
            delegate.write(keys.removeLast(), v);
        }
    }

    @Override
    public void writeNumber(final BigDecimal v) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(v);
        } else if (containerIsArray.getLast()) {
            delegate.write(v);
        } else {
            delegate.write(keys.removeLast(), v);
        }
    }

    @Override
    public void writeNumber(final String encodedValue) {
        if (containerIsArray.isEmpty()) {
            ensureGenerator();
            delegate.write(encodedValue);
        } else if (containerIsArray.getLast()) {
            delegate.write(encodedValue);
        } else {
            delegate.write(keys.removeLast(), encodedValue);
        }
    }

    private void ensureGenerator() {
        delegate = generatorFactory.apply(pretty);
    }
}
