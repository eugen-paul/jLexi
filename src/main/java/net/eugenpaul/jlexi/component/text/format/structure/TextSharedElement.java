package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.pubsub.EventManager;
import net.eugenpaul.jlexi.pubsub.EventSubscriber;
import net.eugenpaul.jlexi.utils.Size;

public class TextSharedElement extends TextStructure implements EventSubscriber {

    private TextStructure sharedElement;
    private EventManager changeManager;

    protected TextSharedElement(TextStructure sharedElement, EventManager changeManager) {
        super(null);
        this.sharedElement = sharedElement;
        this.changeManager = changeManager;
    }

    @Override
    public boolean isEmpty() {
        return this.sharedElement.isEmpty();
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
        this.changeManager.fireEvent(this, SharedEvents.CLEAR, null);
        this.sharedElement.clear();
    }

    public void clearInside() {
        setRepresentation(null);
    }

    @Override
    protected ListIterator<TextStructure> childListIterator() {
        return List.of(sharedElement).listIterator();
    }

    @Override
    protected ListIterator<TextStructure> childListIterator(int index) {
        return childListIterator();
    }

    @Override
    protected TextStructure getFirstChild() {
        return this.sharedElement;
    }

    @Override
    protected TextStructure getLastChild() {
        return this.sharedElement;
    }

    @Override
    protected TextElement getFirstElement() {
        return this.sharedElement.getFirstElement();
    }

    @Override
    protected TextElement getLastElement() {
        return this.sharedElement.getLastElement();
    }

    @Override
    protected Optional<TextStructure> getPreviousChild(TextStructure position) {
        return Optional.empty();
    }

    @Override
    protected Optional<TextStructure> getNextChild(TextStructure position) {
        return Optional.empty();
    }

    @Override
    public void notifyChangeUp() {
        this.changeManager.fireEvent(this, SharedEvents.NOTIFY_CHANGE_UP, null);
        super.notifyChangeUp();
    }

    @Override
    public void notifyChangeDown() {
        this.changeManager.fireEvent(this, SharedEvents.NOTIFY_CHANGE_DOWN, null);
        super.notifyChangeDown();
    }

    @Override
    protected void updateParentOfChildRecursiv() {
        // noothing to do
    }

    @Override
    public void update(Object source, Object type, Object data) {
        if (source != this && type instanceof SharedEvents) {
            SharedEvents event = (SharedEvents) type;
            switch (event) {
            case NOTIFY_CHANGE_UP:
                super.notifyChangeUp();
                break;
            case NOTIFY_CHANGE_DOWN:
                super.notifyChangeDown();
                break;
            case CLEAR:
                clearInside();
                break;
            default:
                break;
            }
        }
    }

    @Override
    public List<TextRepresentation> getRepresentation(Size size) {
        this.sharedElement.setRepresentation(null);
        return this.sharedElement.getRepresentation(size);
    }

}
