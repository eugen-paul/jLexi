package net.eugenpaul.jlexi.component.text.converter.json.format.transformation;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import net.eugenpaul.jlexi.component.text.format.element.TextFormat;

public class TextFormatSerializer extends StdSerializer<TextFormat> {

    public TextFormatSerializer() {
        super(TextFormat.class);
    }

    public TextFormatSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(TextFormat value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();

        gen.writeBooleanField("bold", value.getBold());
        gen.writeBooleanField("italic", value.getItalic());
        gen.writeNumberField("fontsize", value.getFontsize());
        gen.writeStringField("fontName", value.getFontName());
        gen.writeStringField("fontColor", value.getFontColor().getHexArgb());
        gen.writeStringField("backgroundColor", value.getBackgroundColor().getHexArgb());

        gen.writeEndObject();
    }

}
