package com.meossamos.smore.domain.chat.webMedia.service;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

public class JsonStringMessageDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException{
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode node = mapper.readTree(jsonParser);

        return mapper.writeValueAsString(node);
    }
}
