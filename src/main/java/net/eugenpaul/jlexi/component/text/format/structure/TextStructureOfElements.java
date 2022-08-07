package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public abstract class TextStructureOfElements extends TextStructure {

    protected LinkedList<TextElement> children;

    protected TextStructureOfElements(TextStructure parentStructure) {
        super(parentStructure);
        this.children = new LinkedList<>();
    }

    protected Optional<TextElement> getNext(TextElement child) {
        var iterator = this.children.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == child) {
                if (iterator.hasNext()) {
                    return Optional.of(iterator.next());
                }
                break;
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    protected void removeChild(TextElement elementToRemove) {
        var iterator = this.children.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == elementToRemove) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    protected TextElement getFirstElement() {
        if (isEmpty()) {
            return null;
        }
        return this.children.peekFirst();
    }

    @Override
    protected TextElement getLastElement() {
        if (isEmpty()) {
            return null;
        }
        return this.children.peekLast();
    }

    @Override
    protected ListIterator<TextStructure> childListIterator() {
        return Collections.emptyListIterator();
    }

    @Override
    protected ListIterator<TextStructure> childListIterator(int index) {
        return Collections.emptyListIterator();
    }

    @Override
    protected TextStructure getFirstChild() {
        return null;
    }

    @Override
    protected TextStructure getLastChild() {
        return null;
    }

    @Override
    public Optional<Boolean> isABeforB(TextElement elemA, TextElement elemB) {
        for (var child : this.children) {
            if (child == elemA) {
                return Optional.of(Boolean.TRUE);
            }
            if (child == elemB) {
                return Optional.of(Boolean.FALSE);
            }
        }

        // TODO: Expand the function if TextElement is a compositor.

        return Optional.empty();
    }

    @Override
    public List<TextElement> getAllTextElements() {
        return new LinkedList<>(this.children);
    }

    @Override
    public List<TextElement> getAllTextElementsFrom(TextElement from) {
        List<TextElement> response = new LinkedList<>();

        boolean doAdd = false;
        for (var child : this.children) {
            if (from == child) {
                response.add(child);
                doAdd = true;
            } else if (doAdd) {
                response.add(child);
            }
        }

        return response;
    }

    @Override
    public List<TextElement> getAllTextElementsTo(TextElement to) {
        List<TextElement> response = new LinkedList<>();

        for (var child : this.children) {
            if (to == child) {
                response.add(child);
                break;
            } else {
                response.add(child);
            }
        }

        return response;
    }

    @Override
    public List<TextElement> getAllTextElementsBetween(TextElement from, TextElement to) {
        List<TextElement> response = new LinkedList<>();

        boolean doAdd = false;

        for (var child : this.children) {
            if ((from == child && to == child) || to == child) {
                response.add(child);
                break;
            } else if (from == child) {
                response.add(child);
                doAdd = true;
            } else if (doAdd) {
                response.add(child);
            }
        }

        return response;
    }
}
