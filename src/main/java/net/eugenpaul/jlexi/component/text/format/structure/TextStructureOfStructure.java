package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import lombok.var;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public abstract class TextStructureOfStructure extends TextStructure {

    protected LinkedList<TextStructure> children;

    protected TextStructureOfStructure(TextStructure parentStructure) {
        super(parentStructure);
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

    @Override
    protected void restructChildren() {
        var iterator = childListIterator();

        while (iterator.hasNext()) {
            var child = iterator.next();
            if (child.isEmpty()) {
                iterator.remove();
            } else {
                child.restructChildren();
                if (!child.getSplits().isEmpty()) {
                    child.getSplits().stream()//
                            .forEach(iterator::add);
                    child.clearSplitter();
                }
            }
        }
    }

    @Override
    public Optional<Boolean> isABeforB(TextElement elemA, TextElement elemB) {
        List<TextStructure> posAPath = getElementPath(elemA);
        List<TextStructure> posBPath = getElementPath(elemB);

        var iteratorA = posAPath.iterator();
        var iteratorB = posBPath.iterator();

        TextStructure lastStructure = null;
        TextStructure parentA = null;
        TextStructure parentB = null;

        while (iteratorA.hasNext() && iteratorB.hasNext()) {
            parentA = iteratorA.next();
            parentB = iteratorB.next();

            if (parentA != parentB) {
                break;
            }
            lastStructure = parentA;
        }

        if (lastStructure == null) {
            return Optional.empty();
        }

        if (lastStructure != this) {
            return lastStructure.isABeforB(elemA, elemB);
        }

        if (parentA == null || parentB == null) {
            return Optional.empty();
        }

        for (var child : this.children) {
            if (child == parentA) {
                return Optional.of(Boolean.TRUE);
            }
            if (child == parentB) {
                return Optional.of(Boolean.FALSE);
            }
        }

        return Optional.empty();
    }

    @Override
    public List<TextElement> getAllTextElements() {
        List<TextElement> response = new LinkedList<>();

        for (var child : this.children) {
            response.addAll(child.getAllTextElements());
        }

        return response;
    }

    @Override
    public List<TextElement> getAllTextElementsFrom(TextElement from) {
        TextStructure childStructure = getChildWithElement(from);

        List<TextElement> response = new LinkedList<>();

        boolean doAdd = false;
        for (var child : this.children) {
            if (childStructure == child) {
                response.addAll(childStructure.getAllTextElementsFrom(from));
                doAdd = true;
            } else if (doAdd) {
                response.addAll(child.getAllTextElements());
            }
        }

        return response;
    }

    @Override
    public List<TextElement> getAllTextElementsTo(TextElement to) {
        TextStructure childStructure = getChildWithElement(to);

        List<TextElement> response = new LinkedList<>();

        for (var child : this.children) {
            if (childStructure == child) {
                response.addAll(childStructure.getAllTextElementsTo(to));
                break;
            } else {
                response.addAll(child.getAllTextElements());
            }
        }

        return response;
    }

    @Override
    public List<TextElement> getAllTextElementsBetween(TextElement from, TextElement to) {
        TextStructure childStructureFrom = getChildWithElement(from);
        TextStructure childStructureTo = getChildWithElement(to);

        List<TextElement> response = new LinkedList<>();

        boolean doAdd = false;

        for (var child : this.children) {
            if (childStructureFrom == child && childStructureTo == child) {
                response.addAll(childStructureFrom.getAllTextElementsBetween(from, to));
                break;
            } else if (childStructureFrom == child) {
                response.addAll(childStructureFrom.getAllTextElementsFrom(from));
                doAdd = true;
            } else if (childStructureTo == child) {
                response.addAll(childStructureTo.getAllTextElementsTo(to));
                break;
            } else if (doAdd) {
                response.addAll(child.getAllTextElements());
            }
        }

        return response;
    }

}
