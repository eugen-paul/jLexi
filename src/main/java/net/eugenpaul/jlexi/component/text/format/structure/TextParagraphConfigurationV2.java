package net.eugenpaul.jlexi.component.text.format.structure;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToLetterWordV2;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextToWordsCompositorV2;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositorImplV2;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositorV2;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class TextParagraphConfigurationV2 {

    @Default
    private TextToWordsCompositorV2 textToWordCompositor = TextElementToLetterWordV2.builder().build();

    @Default
    private TextWordsToRowCompositorV2 textToRowsCompositor = TextWordsToRowCompositorImplV2.builder().build();

    public TextParagraphConfigurationV2 copy() {
        var response = new TextParagraphConfigurationV2();
        response.textToWordCompositor = textToWordCompositor.copy();
        response.textToRowsCompositor = textToRowsCompositor.copy();
        return response;
    }
}
