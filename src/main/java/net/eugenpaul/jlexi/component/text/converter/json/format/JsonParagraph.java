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
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToFullWord;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToLetterWord;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToSyllableWord;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextHyphenation;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextToWordsCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositorImpl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraphConfiguration;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JsonParagraph {

    @JsonSerialize(using = TextHyphenationSerializer.class)
    @JsonDeserialize(using = TextHyphenationDeserializer.class)
    private TextHyphenation hyphenation = TextHyphenation.LETTER;

    private List<JsonElement> elements = Collections.emptyList();

    public TextParagraphConfiguration getTextSectionConfiguration() {
        return TextParagraphConfiguration.builder()//
                .textToWordCompositor(getHyphenationCompositor())//
                .textToRowsCompositor(getRowCompositor())//
                .build();
    }

    private TextToWordsCompositor<TextElement> getHyphenationCompositor() {
        switch (hyphenation) {
        case LETTER:
            return TextElementToLetterWord.builder().build();
        case WORD:
            return TextElementToFullWord.builder().build();
        case SYLLABLE:
            return TextElementToSyllableWord.builder().build();
        default:
            break;
        }
        return TextElementToFullWord.builder().build();
    }

    private TextWordsToRowCompositor getRowCompositor() {
        return TextWordsToRowCompositorImpl.builder().build();
    }
}
