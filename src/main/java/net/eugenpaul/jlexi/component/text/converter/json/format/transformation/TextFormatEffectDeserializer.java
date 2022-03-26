package net.eugenpaul.jlexi.component.text.converter.json.format.transformation;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.IntNode;

import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;

public class TextFormatEffectDeserializer extends JsonDeserializer<TextFormatEffect> {

    @Override
    public TextFormatEffect deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        FormatUnderlineType underline = null;
        Integer underlineColor = null;
        Integer fontColor = null;
        Integer backgroundColor = null;

        var builder = TextFormatEffect.builder();

        if (node.has("underline")) {
            underline = FormatUnderlineType.valueOf(node.get("underline").asText());
            builder.underline(underline);
        }
        if (node.has("underlineColor")) {
            underlineColor = (Integer) ((IntNode) node.get("underlineColor")).numberValue();
            builder.underlineColor(underlineColor);
        }
        if (node.has("fontColor")) {
            fontColor = (Integer) ((IntNode) node.get("fontColor")).numberValue();
            builder.fontColor(fontColor);
        }
        if (node.has("backgroundColor")) {
            backgroundColor = (Integer) ((IntNode) node.get("backgroundColor")).numberValue();
            builder.backgroundColor(backgroundColor);
        }

        return builder.build();
    }

    @Override
    public TextFormatEffect getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return TextFormatEffect.DEFAULT_FORMAT_EFFECT;
    }

}
