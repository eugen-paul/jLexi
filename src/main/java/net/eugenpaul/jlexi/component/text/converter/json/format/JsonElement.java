package net.eugenpaul.jlexi.component.text.converter.json.format;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.converter.json.format.transformation.TextFormatDeserializer;
import net.eugenpaul.jlexi.component.text.converter.json.format.transformation.TextFormatEffectDeserializer;
import net.eugenpaul.jlexi.component.text.converter.json.format.transformation.TextFormatEffectSerializer;
import net.eugenpaul.jlexi.component.text.converter.json.format.transformation.TextFormatSerializer;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JsonElement {

    @JsonSerialize(using = TextFormatSerializer.class)
    @JsonDeserialize(using = TextFormatDeserializer.class)
    private TextFormat format = TextFormat.DEFAULT;

    @JsonSerialize(using = TextFormatEffectSerializer.class)
    @JsonDeserialize(using = TextFormatEffectDeserializer.class)
    private TextFormatEffect formatEffect = TextFormatEffect.DEFAULT_FORMAT_EFFECT;

    private String text = "";

    private boolean eol = false;

    private boolean eos = false;

}
