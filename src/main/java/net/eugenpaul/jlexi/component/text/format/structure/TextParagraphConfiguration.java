package net.eugenpaul.jlexi.component.text.format.structure;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder.Default;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextElementToRowCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

@Builder
@Getter
@Setter
public class TextParagraphConfiguration {
    
    @Default
    private TextCompositor<TextElement> textToRowsCompositor = new TextElementToRowCompositor<>(0, 0);
}
