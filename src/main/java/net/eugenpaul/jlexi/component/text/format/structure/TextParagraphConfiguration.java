package net.eugenpaul.jlexi.component.text.format.structure;

import lombok.Builder.Default;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextElementToLetterWord;
import net.eugenpaul.jlexi.component.text.format.compositor.texttoword.TextToWordsCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.wordtorow.TextWordsToRowCompositorImpl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

@Builder
@Getter
@Setter
public class TextParagraphConfiguration {
    
    @Default
    private TextToWordsCompositor<TextElement> textToWordCompositor = TextElementToLetterWord.builder().build();
    
    @Default
    private TextWordsToRowCompositor textToRowsCompositor = TextWordsToRowCompositorImpl.builder().build();
}
