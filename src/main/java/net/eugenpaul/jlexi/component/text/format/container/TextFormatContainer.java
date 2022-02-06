package net.eugenpaul.jlexi.component.text.format.container;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.iterator.TextPaneElemenIterator;
import net.eugenpaul.jlexi.component.iterator.TextPaneElementToGlyphIterator;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.keyhandler.CursorMove;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class TextFormatContainer<T extends TextPaneElement> extends TextPaneElement {

    @Getter(lombok.AccessLevel.PROTECTED)
    private FormatAttribute format;

    @Getter
    private TextFormatContainer<T> formatParent;
    private List<T> children;

    protected TextFormatContainer(Glyph parent, TextFormatContainer<T> formatParent,
            NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent, textPaneListElement);
        this.formatParent = formatParent;
        this.format = new FormatAttribute();
        children = new LinkedList<>();
        setSize(Size.ZERO_SIZE);
    }

    public void add(T element) {
        children.add(element);
    }

    @Override
    public boolean isCursorHoldable() {
        return true;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return new TextPaneElementToGlyphIterator<>(children);
    }

    @Override
    public Iterator<TextPaneElement> textIterator() {
        return new TextPaneElemenIterator<>(children);
    }

    @Override
    public Drawable getPixels() {
        return DrawableImpl.EMPTY_DRAWABLE;
    }

    @Override
    public boolean moveCursor(CursorMove move, Cursor cursor) {
        return false;
    }

    @Override
    public NodeListElement<TextPaneElement> getChildOn(Integer mouseX, Integer mouseY) {
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub
    }

    public FormatAttribute requestFormat(FormatAttribute request) {
        request.merge(this.format);
        if (request.isFull() || formatParent == null) {
            return request;
        }

        return formatParent.requestFormat(request);
    }

}
