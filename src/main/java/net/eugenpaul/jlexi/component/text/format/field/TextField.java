package net.eugenpaul.jlexi.component.text.format.field;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public abstract class TextField implements GlyphIterable<TextElement> {
    private TextStructure structureParent;
    private FormatAttribute format;

    protected TextField(TextStructure structureParent, FormatAttribute format) {
        this.structureParent = structureParent;
        this.format = format;
    }

    public abstract TextElement getFirstChild();

    public abstract TextElement getLastChild();

    public void notifyChange() {
        if (null != structureParent) {
            structureParent.notifyChange();
        }
    }

    public abstract boolean isEmpty();

    public FormatAttribute mergeFormat(FormatAttribute format) {
        format.merge(this.format);
        return format;
    }

    public abstract void remove();

    public abstract void addBefore(TextElement position, TextElement element);

    public abstract void reset();

    public abstract void merge(TextField other);

    public abstract void createCopy(TextField fromTextElement);
}
