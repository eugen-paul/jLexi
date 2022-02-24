package net.eugenpaul.jlexi.component.text.format;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public interface CursorControl {
    public TextElement getNext(TextElement element);

    public TextElement getPrevious(TextElement element);

    public TextElement getUp(TextElement element);

    public TextElement getDown(TextElement element);

    public TextElement getEol(TextElement element);

    public TextElement getBol(TextElement element);

    public TextElement getStart(TextElement element);

    public TextElement getEnd(TextElement element);

    public TextElement getPageUp(TextElement element);

    public TextElement getPageDown(TextElement element);
}
