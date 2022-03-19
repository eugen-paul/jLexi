package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public abstract class TextStructureOfStructure extends TextStructure {

    protected LinkedList<TextStructure> children;

    protected TextStructureOfStructure(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        super(parentStructure, format, storage);
        this.children = new LinkedList<>();
    }

    @Override
    public void clear() {
        children.clear();
        resetStructure();
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
    }

    @Override
    protected ListIterator<TextStructure> childListIterator() {
        return children.listIterator();
    }

    @Override
    protected TextStructure getFirstChild() {
        return children.peekFirst();
    }

    @Override
    protected TextStructure getLastChild() {
        return children.peekLast();
    }

    @Override
    protected TextElement getFirstElement() {
        if (isEmpty()) {
            return null;
        }
        return children.peekFirst().getFirstElement();
    }

    @Override
    protected TextElement getLastElement() {
        if (isEmpty()) {
            return null;
        }
        return children.peekLast().getLastElement();
    }

}
