package net.eugenpaul.jlexi.component.text.converter.json.format.transformation;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextHyphenation;

public class TextHyphenationDeserializer extends JsonDeserializer<TextHyphenation> {

    @Override
    public TextHyphenation deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        return TextHyphenation.fromString(node.textValue());
    }

    @Override
    public TextHyphenation getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return TextHyphenation.WORD;
    }

}
