package net.eugenpaul.jlexi.component.text.format.structure;

import lombok.Builder.Default;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToLetterWord;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextToWordsCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositorImpl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class TextParagraphConfiguration {

    @Default
    private TextToWordsCompositor<TextElement> textToWordCompositor = TextElementToLetterWord.builder().build();

    @Default
    private TextWordsToRowCompositor textToRowsCompositor = TextWordsToRowCompositorImpl.builder().build();

    public TextParagraphConfiguration copy() {
        var response = new TextParagraphConfiguration();
        response.textToWordCompositor = textToWordCompositor.copy();
        response.textToRowsCompositor = textToRowsCompositor.copy();
        return response;
    }
}
