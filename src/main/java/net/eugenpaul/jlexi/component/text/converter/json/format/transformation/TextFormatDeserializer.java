package net.eugenpaul.jlexi.component.text.converter.json.format.transformation;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;

public class TextFormatDeserializer extends JsonDeserializer<TextFormat> {

    @Override
    public TextFormat deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        Boolean bold = null;
        Boolean italic = null;
        Integer fontsize = null;
        String fontName = null;

        var builder = TextFormat.builder();
        if (node.has("bold")) {
            bold = ((BooleanNode) node.get("bold")).booleanValue();
            builder.bold(bold);
        }
        if (node.has("italic")) {
            italic = ((BooleanNode) node.get("italic")).booleanValue();
            builder.italic(italic);
        }
        if (node.has("fontsize")) {
            fontsize = (Integer) ((IntNode) node.get("fontsize")).numberValue();
            builder.fontsize(fontsize);
        }
        if (node.has("fontName")) {
            fontName = node.get("fontName").asText();
            builder.fontName(fontName);
        }

        return builder.build();
    }

    @Override
    public TextFormat getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return TextFormat.DEFAULT;
    }

}
