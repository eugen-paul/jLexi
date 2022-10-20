package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.pubsub.EventManager;
import net.eugenpaul.jlexi.pubsub.EventSubscriber;

public class TextReference extends TextStructure implements EventSubscriber {

    private TextStructure reference;
    private EventManager changeManager;

    protected TextReference(TextStructure reference, EventManager changeManager) {
        super(null);
        this.reference = reference;
        this.changeManager = changeManager;
    }

    @Override
    public boolean isEmpty() {
        return reference.isEmpty();
    }

    @Override
    protected boolean checkMergeWith(TextStructure element) {
        return false;
    }

    @Override
    public TextAddResponse splitChild(TextStructure child, List<TextStructure> to) {
        return null;
    }

    @Override
    protected TextRemoveResponse mergeWith(TextStructure element) {
        return null;
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructure child) {
        return null;
    }

    @Override
    public Optional<Boolean> isABeforB(TextElement elemA, TextElement elemB) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public List<TextElement> getAllTextElements() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElement> getAllTextElementsBetween(TextElement from, TextElement to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElement> getAllTextElementsFrom(TextElement from) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TextElement> getAllTextElementsTo(TextElement to) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub

    }

    @Override
    protected ListIterator<TextStructure> childListIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ListIterator<TextStructure> childListIterator(int index) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TextStructure getFirstChild() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TextStructure getLastChild() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TextElement getFirstElement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TextElement getLastElement() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void update(Object source, Object type, Object data) {
        if (source != this) {
            // TODO Auto-generated method stub
        }
    }

}
