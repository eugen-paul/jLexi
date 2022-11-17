package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.pubsub.EventManager;
import net.eugenpaul.jlexi.pubsub.EventSubscriber;
import net.eugenpaul.jlexi.utils.Size;

public class TextSharedElementV2 extends TextStructureV2 implements EventSubscriber {

    private TextStructureV2 sharedElement;
    private EventManager changeManager;

    protected TextSharedElementV2(TextStructureV2 sharedElement, EventManager changeManager) {
        super(null);
        this.sharedElement = sharedElement;
        this.changeManager = changeManager;
    }

    protected TextSharedElementV2(TextSharedElementV2 other) {
        super(null);
        this.sharedElement = other.sharedElement;
        this.changeManager = other.changeManager;
    }

    @Override
    public boolean isEmpty() {
        return this.sharedElement.isEmpty();
    }

    @Override
    protected boolean checkMergeWith(TextStructureV2 element) {
        return false;
    }

    @Override
    public TextAddResponseV2 replaceChild(TextStructureV2 child, List<TextStructureV2> to) {
        return TextAddResponseV2.EMPTY;
    }
    
    @Override
    protected TextRemoveResponseV2 mergeWith(TextStructureV2 element) {
        return TextRemoveResponseV2.EMPTY;
    }

    @Override
    protected TextRemoveResponseV2 mergeChildsWithNext(TextStructureV2 child) {
        return TextRemoveResponseV2.EMPTY;
    }

    @Override
    public Optional<Boolean> isABeforB(TextElementV2 elemA, TextElementV2 elemB) {
        // TODO Auto-generated method stub
        return Optional.empty();
    }

    @Override
    public TextStructureV2 getSelectedAll() {
        //TODO
        return this;
    }

    @Override
    public TextStructureV2 getSelectedBetween(TextElementV2 from, TextElementV2 to) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public TextStructureV2 getSelectedFrom(TextElementV2 from) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public TextStructureV2 getSelectedTo(TextElementV2 to) {
        // TODO Auto-generated method stub
        return this;
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
    protected ListIterator<TextStructureV2> childListIterator() {
        return List.of(sharedElement).listIterator();
    }

    @Override
    protected ListIterator<TextStructureV2> childListIterator(int index) {
        return childListIterator();
    }

    @Override
    protected TextStructureV2 getFirstChild() {
        return this.sharedElement;
    }

    @Override
    protected TextStructureV2 getLastChild() {
        return this.sharedElement;
    }

    @Override
    protected Optional<TextStructureV2> getPreviousChild(TextStructureV2 position) {
        return Optional.empty();
    }

    @Override
    protected Optional<TextStructureV2> getNextChild(TextStructureV2 position) {
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
    public List<TextRepresentationV2> getRepresentation(Size size) {
        this.sharedElement.setRepresentation(null);
        return this.sharedElement.getRepresentation(size);
    }

}
