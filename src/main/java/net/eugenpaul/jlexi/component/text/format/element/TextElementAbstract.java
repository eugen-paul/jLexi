package net.eugenpaul.jlexi.component.text.format.element;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class TextElementAbstract extends TextElement {

    protected FontStorage fontStorage;

    protected TextElementAbstract(Glyph parent, FontStorage fontStorage) {
        super(parent);
        this.fontStorage = fontStorage;
    }

    @Override
    public boolean isCursorHoldable() {
        return false;
    }

    @Override
    public Iterator<Glyph> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }

}