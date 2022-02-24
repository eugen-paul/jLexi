package net.eugenpaul.jlexi.component.text.format.field;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.CursorControl;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public abstract class TextField implements CursorControl {
    private TextStructure structureParent;
    private FormatAttribute format;

    protected TextField(TextStructure structureParent, FormatAttribute format) {
        this.structureParent = structureParent;
        this.format = format;
    }

    public abstract Iterator<TextElement> printableIterator();

    public abstract TextElement getFirstChild();

    public abstract TextElement getLastChild();

    public abstract void notifyChange();

    public abstract boolean isEmpty();

    public abstract FormatAttribute mergeFormat(FormatAttribute format);

    public abstract void remove();

    public abstract void addBefor(TextElement position, TextElement element);

    public abstract void reset();

    public abstract <T> List<T> split();

    public abstract void merge(TextField other);

    public abstract void createCopy(TextField fromTextElement);
}
