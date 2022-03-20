package net.eugenpaul.jlexi.component.text.converter.json.format.transformation;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;

public class TextFormatEffectSerializer extends StdSerializer<TextFormatEffect> {

    public TextFormatEffectSerializer() {
        super(TextFormatEffect.class);
    }

    public TextFormatEffectSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(TextFormatEffect value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeStringField("underline", value.getUnderline().toString());
        gen.writeNumberField("underlineColor", value.getUnderlineColor());
        gen.writeNumberField("fontColor", value.getFontColor());
        gen.writeNumberField("backgroundColor", value.getBackgroundColor());

        gen.writeEndObject();
    }

}
