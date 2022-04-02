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
import net.eugenpaul.jlexi.utils.Color;

public class TextFormatDeserializer extends JsonDeserializer<TextFormat> {

    @Override
    public TextFormat deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        Boolean bold = null;
        Boolean italic = null;
        Integer fontsize = null;
        String fontName = null;
        Color fontColor = null;
        Color backgroundColor = null;

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
        if (node.has("fontColor")) {
            fontColor = Color.fromText(node.get("fontColor").asText());
            builder.fontColor(fontColor);
        }
        if (node.has("backgroundColor")) {
            backgroundColor = Color.fromText(node.get("backgroundColor").asText());
            builder.backgroundColor(backgroundColor);
        }

        return builder.build();
    }

    @Override
    public TextFormat getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return TextFormat.DEFAULT;
    }

}
