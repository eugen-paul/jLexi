package net.eugenpaul.jlexi.component.text.converter.json.format.transformation;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextHyphenation;

public class TextHyphenationSerializer extends StdSerializer<TextHyphenation> {

    public TextHyphenationSerializer() {
        super(TextHyphenation.class);
    }

    public TextHyphenationSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    @Override
    public void serialize(TextHyphenation value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.getValue());
    }

}
