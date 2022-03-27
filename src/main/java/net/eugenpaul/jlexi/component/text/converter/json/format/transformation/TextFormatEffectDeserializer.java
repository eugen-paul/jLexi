package net.eugenpaul.jlexi.component.text.converter.json.format.transformation;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;
import net.eugenpaul.jlexi.utils.Color;

public class TextFormatEffectDeserializer extends JsonDeserializer<TextFormatEffect> {

    @Override
    public TextFormatEffect deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        FormatUnderlineType underline = null;
        Color underlineColor = null;
        Color fontColor = null;
        Color backgroundColor = null;

        var builder = TextFormatEffect.builder();

        if (node.has("underline")) {
            underline = FormatUnderlineType.valueOf(node.get("underline").asText());
            builder.underline(underline);
        }
        if (node.has("underlineColor")) {
            underlineColor = Color.fromText(node.get("underlineColor").asText());
            builder.underlineColor(underlineColor);
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
    public TextFormatEffect getNullValue(DeserializationContext ctxt) throws JsonMappingException {
        return TextFormatEffect.DEFAULT_FORMAT_EFFECT;
    }

}
