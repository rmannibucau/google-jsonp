package com.github.rmannibucau.google.json;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Objects.requireNonNull;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.json.Json;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParserFactory;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.api.client.json.JsonParser;

public class JsonpJsonFactory extends JsonFactory {
    private final JsonParserFactory parserFactory;
    private final JsonGeneratorFactory generatorFactory;
    private final JsonGeneratorFactory prettyGeneratorFactory;

    private JsonpJsonFactory(final JsonParserFactory parserFactory,
                             final JsonGeneratorFactory generatorFactory,
                             final JsonGeneratorFactory prettyGeneratorFactory) {
        this.parserFactory = parserFactory;
        this.generatorFactory = generatorFactory;
        this.prettyGeneratorFactory = prettyGeneratorFactory;
    }

    @Override
    public JsonParser createJsonParser(final InputStream inputStream) {
        return new JsonParserImpl(this, parserFactory.createParser(requireNonNull(inputStream)));
    }

    @Override
    public JsonParser createJsonParser(final InputStream inputStream, final Charset charset) {
        return new JsonParserImpl(this, parserFactory.createParser(requireNonNull(inputStream), charset));
    }

    @Override
    public JsonParser createJsonParser(final String s) {
        return new JsonParserImpl(this, parserFactory.createParser(new StringReader(requireNonNull(s))));
    }

    @Override
    public JsonParser createJsonParser(final Reader reader) {
        return new JsonParserImpl(this, parserFactory.createParser(requireNonNull(reader)));
    }

    @Override
    public JsonGenerator createJsonGenerator(final OutputStream outputStream, final Charset charset) {
        return new JsonGeneratorImpl(this,
                pretty -> (pretty ? prettyGeneratorFactory : generatorFactory).createGenerator(outputStream, charset));
    }

    @Override
    public JsonGenerator createJsonGenerator(final Writer writer) {
        return new JsonGeneratorImpl(this,
                pretty -> (pretty ? prettyGeneratorFactory : generatorFactory).createGenerator(writer));
    }

    public static JsonFactory of() {
        return of(
                Json.createParserFactory(emptyMap()),
                Json.createGeneratorFactory(emptyMap()),
                Json.createGeneratorFactory(singletonMap(javax.json.stream.JsonGenerator.PRETTY_PRINTING, "true")));
    }

    public static JsonFactory of(final JsonParserFactory parserFactory, final JsonGeneratorFactory generatorFactory) {
        return new JsonpJsonFactory(parserFactory, generatorFactory, generatorFactory);
    }

    public static JsonFactory of(final JsonParserFactory parserFactory,
                                 final JsonGeneratorFactory generatorFactory,
                                 final JsonGeneratorFactory prettyGeneratorFactory) {
        return new JsonpJsonFactory(parserFactory, generatorFactory, prettyGeneratorFactory);
    }
}
