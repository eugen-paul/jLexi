package net.eugenpaul.jlexi.component.text.format.field;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.Empty;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.Splitable;
import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraph;

public abstract class TextField implements GlyphIterable<TextElement>, TextDocumentElement, Splitable<TextField>, Empty {
    @Getter(lombok.AccessLevel.PROTECTED)
    @Setter
    private TextParagraph structureParent;

    @Getter(lombok.AccessLevel.PROTECTED)
    private FormatAttribute format;

    protected boolean edit;

    protected TextField(TextParagraph structureParent, FormatAttribute format) {
        this.structureParent = structureParent;
        this.format = format;
        this.edit = false;
    }

    public abstract TextElement getFirstChild();

    public abstract TextElement getLastChild();

    public void notifyChange() {
        if (null != structureParent) {
            structureParent.notifyChange(edit);
            edit = false;
        }
    }

    public FormatAttribute mergeFormat(FormatAttribute format) {
        format.merge(this.format);
        return format;
    }

    public abstract TextElement remove(TextElement element);

    public abstract void addBefore(TextElement position, TextElement element);

    public abstract void reset();

    public abstract void merge(TextField other);

    public abstract void createCopy(TextField fromTextElement);
}
