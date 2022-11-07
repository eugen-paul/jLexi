package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

public abstract class TextStructureOfStructureV2 extends TextStructureV2 {

    protected LinkedList<TextStructureV2> children;

    protected TextStructureOfStructureV2(TextStructureV2 parentStructure) {
        super(parentStructure);
        this.children = new LinkedList<>();
    }

    protected abstract TextStructureOfStructureV2 createMergedStructute();

    @Override
    protected TextRemoveResponseV2 mergeWith(TextStructureV2 element) {
        if (!checkMergeWith(element)) {
            return TextRemoveResponseV2.EMPTY;
        }

        var nextSection = (TextSectionV2) element;

        // By merging of two Section we must remove the endOfSection-Element at the end of first section. To do this, we
        // must merge the last child with the first child of incomming element.
        var firstStructure = getLastChild();
        var secondStructure = nextSection.getFirstChild();

        var removedData = firstStructure.mergeWith(secondStructure);
        if (removedData == TextRemoveResponseV2.EMPTY) {
            // Structures cann't be merged
            return TextRemoveResponseV2.EMPTY;
        }

        var responseSection = createMergedStructute();

        // take over own child elements except the last
        var iteratorFirst = childListIterator();
        while (iteratorFirst.hasNext()) {
            responseSection.children.add(iteratorFirst.next());
        }

        responseSection.children.removeLast();

        // add new element created by mergeWith
        responseSection.children.addAll(removedData.getNewStructures());

        // take over child elements from following structure except the first
        var iteratorSecond = nextSection.childListIterator(1);
        while (iteratorSecond.hasNext()) {
            responseSection.children.add(iteratorSecond.next());
        }

        responseSection.children.stream().forEach(v -> v.setParentStructure(responseSection));

        return new TextRemoveResponseV2(//
                removedData.getRemovedElement(), //
                removedData.getNewCursorPosition(), //
                this, //
                List.of(this, nextSection), //
                List.of(responseSection) //
        );
    }

    @Override
    public void clear() {
        this.children.clear();
        setRepresentation(null);
    }

    @Override
    public boolean isEmpty() {
        return this.children.isEmpty();
    }

    @Override
    public boolean isEndOfLine() {
        return !isEmpty() && this.children.getLast().isEndOfLine();
    }

    @Override
    public boolean isEndOfSection() {
        return !isEmpty() && this.children.getLast().isEndOfSection();
    }

    @Override
    protected ListIterator<TextStructureV2> childListIterator() {
        return this.children.listIterator();
    }

    @Override
    protected ListIterator<TextStructureV2> childListIterator(int index) {
        return children.listIterator(index);
    }

    @Override
    protected TextStructureV2 getFirstChild() {
        return children.peekFirst();
    }

    @Override
    protected TextStructureV2 getLastChild() {
        return children.peekLast();
    }

    @Override
    public Optional<Boolean> isABeforB(TextElementV2 elemA, TextElementV2 elemB) {
        List<TextStructureV2> posAPath = getElementPath(elemA);
        List<TextStructureV2> posBPath = getElementPath(elemB);

        var iteratorA = posAPath.iterator();
        var iteratorB = posBPath.iterator();

        TextStructureV2 lastStructure = null;
        TextStructureV2 parentA = null;
        TextStructureV2 parentB = null;

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
    public List<TextElementV2> getAllTextElements() {
        List<TextElementV2> response = new LinkedList<>();

        for (var child : this.children) {
            response.addAll(child.getAllTextElements());
        }

        return response;
    }

    @Override
    public List<TextElementV2> getAllTextElementsFrom(TextElementV2 from) {
        TextStructureV2 childStructure = getChildWithElement(from);

        List<TextElementV2> response = new LinkedList<>();

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
    public List<TextElementV2> getAllTextElementsTo(TextElementV2 to) {
        TextStructureV2 childStructure = getChildWithElement(to);

        List<TextElementV2> response = new LinkedList<>();

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
    public List<TextElementV2> getAllTextElementsBetween(TextElementV2 from, TextElementV2 to) {
        TextStructureV2 childStructureFrom = getChildWithElement(from);
        TextStructureV2 childStructureTo = getChildWithElement(to);

        List<TextElementV2> response = new LinkedList<>();

        boolean doAdd = false;

        for (var child : this.children) {
            if (childStructureFrom == child && childStructureTo == child) {
                response.addAll(childStructureFrom.getAllTextElementsBetween(from, to));
            } else if (childStructureFrom == child) {
                response.addAll(childStructureFrom.getAllTextElementsFrom(from));
                doAdd = true;
            } else if (childStructureTo == child) {
                response.addAll(childStructureTo.getAllTextElementsTo(to));
            } else if (doAdd) {
                response.addAll(child.getAllTextElements());
            }

            if (childStructureTo == child) {
                break;
            }
        }

        return response;
    }

}
