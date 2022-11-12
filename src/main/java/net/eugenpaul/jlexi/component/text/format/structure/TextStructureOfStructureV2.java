package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
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
                // removedData.getRemovedElement(), //
                removedData.getNewCursorPosition(), //
                this, //
                List.of(this, nextSection), //
                List.of(responseSection) //
        );
    }

    @Override
    protected TextRemoveResponseV2 mergeChildsWithNext(TextStructureV2 child) {
        var nextChild = getNextChild(child);

        if (nextChild.isPresent()) {
            var removedData = child.mergeWith(nextChild.get());
            if (removedData != TextRemoveResponseV2.EMPTY) {
                var iterator = childListIterator();
                while (iterator.hasNext()) {
                    // TODO do it better
                    var currentChild = iterator.next();
                    if (currentChild == child) {
                        iterator.remove();
                        iterator.next();
                        iterator.remove();

                        removedData.getNewStructures().forEach(v -> {
                            iterator.add(v);
                            v.setParentStructure(this);
                        });

                        break;
                    }
                }
            }

            notifyChangeDown();
            notifyChangeUp();

            return new TextRemoveResponseV2(//
                    // removedData.getRemovedElement(), //
                    removedData.getNewCursorPosition(), //
                    this, //
                    removedData.getRemovedStructures(), //
                    removedData.getNewStructures() //
            );
        } else if (getParentStructure() != null) {
            return getParentStructure().mergeChildsWithNext(this);
        }
        return TextRemoveResponseV2.EMPTY;
    }

    @Override
    public TextRemoveResponseV2 removeElement(TextStructureV2 elementToRemove) {
        var childToRemove = getChildWithElement(elementToRemove);
        if (childToRemove == null) {
            return TextRemoveResponseV2.EMPTY;
        }

        if (childToRemove == elementToRemove) {
            // delete own child node
            var nextElement = getNextChild(elementToRemove);

            if (nextElement.isEmpty()) {
                // There is no following element. Try to merge the paragraph with the following paragraph.
                if (getParentStructure() != null) {
                    return getParentStructure().mergeChildsWithNext(this);
                }
                return TextRemoveResponseV2.EMPTY;
            }

            removeChild(elementToRemove);

            notifyChangeUp();

            return new TextRemoveResponseV2(//
                    null, // TODO each TextStructureV2 should have a position
                    this, //
                    List.of(elementToRemove), //
                    Collections.emptyList() //
            );
        }

        return childToRemove.removeElement(elementToRemove);
    }

    protected abstract boolean hasToBeSplited(TextStructureV2 newChild);

    @Override
    public TextAddResponseV2 replaceChild(TextStructureV2 child, List<TextStructureV2> to) {

        boolean splitNeed = to.stream().anyMatch(this::hasToBeSplited);

        if (splitNeed && getParentStructure() != null) {
            var splitResult = replaceAndSplit(child, to);
            return getParentStructure().replaceChild(this, splitResult);
        }

        var chiltIterator = this.children.listIterator();
        while (chiltIterator.hasNext()) {
            var elem = chiltIterator.next();
            if (elem == child) {
                chiltIterator.remove();
                to.forEach(chiltIterator::add);
                to.forEach(v -> v.setParentStructure(this));

                notifyChangeUp();

                return new TextAddResponseV2(//
                        this, //
                        child, //
                        to //
                );
            }
        }

        throw new IllegalArgumentException("Cann't split section. Child to replace not found.");
    }

    private List<TextStructureV2> replaceAndSplit(TextStructureV2 position, List<TextStructureV2> to) {
        var first = createMergedStructute();
        var second = createMergedStructute();

        var current = first;

        var chiltIterator = this.children.listIterator();
        while (chiltIterator.hasNext()) {
            var currentChild = chiltIterator.next();
            if (currentChild == position) {
                var toIterator = to.listIterator();
                while (toIterator.hasNext()) {
                    var toElement = toIterator.next();
                    current.children.add(toElement);
                    toElement.setParentStructure(current);
                    if (hasToBeSplited(toElement)) {
                        current = second;
                    }
                }
            } else {
                current.children.add(currentChild);
                currentChild.setParentStructure(current);
            }
        }

        return List.of(first, second);
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

    protected void removeChild(TextStructureV2 elementToRemove) {
        var iterator = this.children.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next() == elementToRemove) {
                iterator.remove();
                break;
            }
        }
    }

}
