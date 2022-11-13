package net.eugenpaul.jlexi.component.text.converter.json.format;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.converter.json.format.transformation.TextHyphenationDeserializer;
import net.eugenpaul.jlexi.component.text.converter.json.format.transformation.TextHyphenationSerializer;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToFullWordV2;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToLetterWordV2;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToSyllableWordV2;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextHyphenation;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextToWordsCompositorV2;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositorImplV2;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositorV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraphConfigurationV2;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JsonParagraphV2 {

    @JsonSerialize(using = TextHyphenationSerializer.class)
    @JsonDeserialize(using = TextHyphenationDeserializer.class)
    private TextHyphenation hyphenation = TextHyphenation.LETTER;

    private List<JsonElement> elements = Collections.emptyList();

    public TextParagraphConfigurationV2 getTextSectionConfiguration() {
        return TextParagraphConfigurationV2.builder()//
                .textToWordCompositor(getHyphenationCompositor())//
                .textToRowsCompositor(getRowCompositor())//
                .build();
    }

    private TextToWordsCompositorV2 getHyphenationCompositor() {
        switch (hyphenation) {
        case LETTER:
            return TextElementToLetterWordV2.builder().build();
        case WORD:
            return TextElementToFullWordV2.builder().build();
        case SYLLABLE:
            return TextElementToSyllableWordV2.builder().build();
        default:
            break;
        }
        return TextElementToFullWordV2.builder().build();
    }

    private TextWordsToRowCompositorV2 getRowCompositor() {
        return TextWordsToRowCompositorImplV2.builder().build();
    }
}
