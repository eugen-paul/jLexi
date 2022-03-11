package net.eugenpaul.jlexi.component.text.format.element;

import java.util.Collections;
import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.visitor.Visitor;

public abstract class TextElementAbstract extends TextElement {

    protected ResourceManager storage;

    protected TextElementAbstract(Glyph parent, ResourceManager storage, TextStructure parentTextField,
            TextFormat format) {
        super(parent, parentTextField, format);
        this.storage = storage;
    }

    @Override
    public boolean isCursorHoldable() {
        return false;
    }

    @Override
    public boolean isRemoveable() {
        return true;
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
