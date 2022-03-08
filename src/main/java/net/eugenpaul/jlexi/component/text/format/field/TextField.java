package net.eugenpaul.jlexi.component.text.format.field;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.Splitable;
import net.eugenpaul.jlexi.component.text.format.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public abstract class TextField implements GlyphIterable<TextElement>, TextFormat, Splitable<TextField> {
    @Getter(lombok.AccessLevel.PROTECTED)
    @Setter
    private TextStructure structureParent;

    @Getter(lombok.AccessLevel.PROTECTED)
    private FormatAttribute format;

    protected TextField(TextStructure structureParent, FormatAttribute format) {
        this.structureParent = structureParent;
        this.format = format;
    }

    public abstract TextElement getFirstChild();

    public abstract TextElement getLastChild();

    public void notifyChange() {
        if (null != structureParent) {
            structureParent.notifyChange(!getSplits().isEmpty());
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
