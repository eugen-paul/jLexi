package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;
import java.util.function.Supplier;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public abstract class TextStructure {
    private List<TextStructure> children;
    private Supplier<TextStructure> childConstructor;
    private FormatAttribute format;
    private List<TextStructure> structure;
    // private TextPaneRowCompositor compositor;
    private TextStructure parentStructure;

    public abstract void setFormat(TextElement from, TextElement to);

    // getRows(length: int);
    // setFormat(from, to);
    // remove(from, to);

    // T merge(other: T);
    // split();
}
