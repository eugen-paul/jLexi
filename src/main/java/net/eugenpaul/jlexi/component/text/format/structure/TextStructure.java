package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public abstract class TextStructure {
    private List<TextStructure> children;
    // private Supplier<TextStructure> childConstructor;
    private FormatAttribute format;
    private List<TextStructure> structure;
    protected TextCompositor compositor;
    private TextStructure parentStructure;

    protected TextStructure(FormatAttribute format) {
        this.format = format;
        this.children = new LinkedList<>();
    }

    public FormatAttribute mergeFormat(FormatAttribute format) {
        format = format.merge(this.format);
        if (null != parentStructure) {
            format = parentStructure.mergeFormat(format);
        }
        return format;
    }

    public abstract void setFormat(TextElement from, TextElement to);

    public abstract void notifyChange();

    public abstract List<TextStructure> getRows(int length);
    // setFormat(from, to);
    // remove(from, to);

    // T merge(other: T);
    // split();
}
