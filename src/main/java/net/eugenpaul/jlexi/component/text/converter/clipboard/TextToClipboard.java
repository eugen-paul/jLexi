package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.awt.datatransfer.Transferable;
import java.util.List;

import lombok.NoArgsConstructor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

@NoArgsConstructor
public class TextToClipboard {

    Transferable generateSelection(List<TextElement> text) {
        return new ToClipboardSelection(text);
    }
}
