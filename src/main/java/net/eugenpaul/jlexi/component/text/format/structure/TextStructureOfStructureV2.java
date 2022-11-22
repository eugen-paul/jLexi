package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.utils.Size;

public abstract class TextStructureOfStructureV2 extends TextStructureV2 {

    protected LinkedList<TextStructureV2> children;

    protected TextStructureOfStructureV2(TextStructureV2 parentStructure) {
        super(parentStructure);
        this.children = new LinkedList<>();
    }

    protected abstract TextStructureOfStructureV2 copyStructure();

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

        var responseSection = copyStructure();

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
        // TODO
        var pathToRemove = getChildWithElement(elementToRemove);
        if (pathToRemove == null) {
            return TextRemoveResponseV2.EMPTY;
        }

        if (pathToRemove == elementToRemove) {
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

        return pathToRemove.removeElement(elementToRemove);
    }

    /**
     * Returns the list of {@code TextRepresentation} that represent the object. Depending on the type, the
     * {@code TextRepresentation} object can be a row, a column, a page or other.
     * 
     * @param size - max size of the returned object
     * @return response
     */
    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        if (getRepresentation() != null) {
            return getRepresentation();
        }

        List<TextRepresentationV2> newRepresentation = new LinkedList<>();
        var iterator = childListIterator();
        while (iterator.hasNext()) {
            newRepresentation.addAll(iterator.next().getRepresentation(size));
        }

        setRepresentation(newRepresentation);

        return getRepresentation();
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
        var first = copyStructure();
        var second = copyStructure();

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
        var pathToA = getChildWithElement(elemA);
        var pathToB = getChildWithElement(elemB);

        if (pathToA != null && pathToA == pathToB) {
            return pathToA.isABeforB(elemA, elemB);
        }

        if (pathToA == null) {
            return Optional.empty();
        }

        if (pathToB == null) {
            return Optional.empty();
        }

        for (var child : this.children) {
            if (child == pathToA) {
                return Optional.of(Boolean.TRUE);
            }
            if (child == pathToB) {
                return Optional.of(Boolean.FALSE);
            }
        }

        return Optional.empty();
    }

    @Override
    public TextStructureV2 getSelectedAll() {
        var root = copyStructure();

        for (var child : this.children) {
            root.children.add(child.getSelectedAll());
        }

        return root;
    }

    @Override
    public TextStructureV2 getSelectedFrom(TextElementV2 from) {
        var root = copyStructure();

        var pathToFrom = getChildWithElement(from);

        if (pathToFrom == null) {
            return root;
        }

        boolean doAdd = false;
        for (var child : this.children) {
            if (pathToFrom == child) {
                root.children.add(pathToFrom.getSelectedFrom(from));
                doAdd = true;
            } else if (doAdd) {
                root.children.add(child.getSelectedAll());
            }
        }

        return root;
    }

    @Override
    public TextStructureV2 getSelectedTo(TextElementV2 to) {
        var root = copyStructure();

        var pathToTo = getChildWithElement(to);

        if (pathToTo == null) {
            return root;
        }

        for (var child : this.children) {
            if (pathToTo == child) {
                root.children.add(pathToTo.getSelectedTo(to));
                break;
            } else {
                root.children.add(child.getSelectedAll());
            }
        }

        return root;
    }

    @Override
    public TextStructureV2 getSelectedBetween(TextElementV2 from, TextElementV2 to) {
        var root = copyStructure();

        var pathToA = getChildWithElement(from);
        var pathToB = getChildWithElement(to);

        if (pathToA == null || pathToB == null) {
            return root;
        }

        boolean doAdd = false;

        for (var child : this.children) {
            if (pathToA == child && pathToB == child) {
                root.children.add(pathToA.getSelectedBetween(from, to));
            } else if (pathToA == child) {
                root.children.add(pathToA.getSelectedFrom(from));
                doAdd = true;
            } else if (pathToB == child) {
                root.children.add(pathToB.getSelectedTo(to));
            } else if (doAdd) {
                root.children.add(child.getSelectedAll());
            }

            if (pathToB == child) {
                break;
            }
        }

        return root;
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

    @Override
    public TextAddResponseV2 addBefore(TextStructureV2 position, TextCopyData element) {
        var canBeProcessed = element.getElements().stream().allMatch(this::canContainChild);

        if (!canBeProcessed) {
            return super.addBefore(position, element);
        }

        var pathToPosition = getChildWithElement(position);
        if (pathToPosition == position) {
            return doInsertBefore(position, element.getElements().listIterator());
        } else if (pathToPosition == null) {
            return TextAddResponseV2.EMPTY;
        }

        return pathToPosition.splitChildsBefore(position, element.getElements().listIterator());
    }

    private TextAddResponseV2 doInsertBefore(TextStructureV2 position, ListIterator<TextStructureV2> dataIterator) {
        if (getParentStructure() == null) {
            return TextAddResponseV2.EMPTY;
        }

        var selfCopy = copyStructure();

        var iterator = childListIterator();
        while (iterator.hasNext()) {
            var currentChild = iterator.next();
            if (currentChild == position) {
                while (dataIterator.hasNext()) {
                    var dataElement = dataIterator.next();
                    dataElement.setParentStructure(this);
                    selfCopy.children.add(dataElement);
                }
            }
            selfCopy.children.add(currentChild);
        }

        return new TextAddResponseV2(//
                getParentStructure(), //
                this, //
                List.of(selfCopy) //
        );
    }

    @Override
    protected TextAddResponseV2 splitBefore(TextStructureV2 position, ListIterator<TextStructureV2> data) {
        var pathToPosition = getChildWithElement(position);

        List<TextStructureV2> newStructures = new LinkedList<>();

        var currentStructure = copyStructure();
        var iterator = childListIterator();

        while (iterator.hasNext()) {
            var currentChild = iterator.next();
            if (pathToPosition != currentChild) {
                currentStructure.children.add(currentChild);
            } else {

            }
        }

        return new TextAddResponseV2(//
                getParentStructure(), //
                this, //
                newStructures //
        );
    }

    protected List<TextStructureV2> splitBefore(TextStructureV2 position, TextStructureV2 first, TextStructureV2 last) {
        var pathToPosition = getChildWithElement(position);

        List<TextStructureV2> response = new LinkedList<>();

        var currentStructure = copyStructure();
        response.add(currentStructure);

        var iterator = childListIterator();

        while (iterator.hasNext()) {
            var currentChild = iterator.next();
            if (pathToPosition != currentChild) {
                currentStructure.children.add(currentChild);
            } else {
                if (pathToPosition == position) {
                    var firstIterator = first.childListIterator();
                    while (firstIterator.hasNext()) {
                        currentStructure.children.add(firstIterator.next());
                    }

                    currentStructure = copyStructure();
                    response.add(currentStructure);

                    var lastIterator = last.childListIterator();
                    while (lastIterator.hasNext()) {
                        currentStructure.children.add(lastIterator.next());
                    }
                } else {
                    // TODO
                }
            }
        }

        return response;
    }

    @Override
    protected TextAddResponseV2 splitChildsBefore(TextStructureV2 position, ListIterator<TextStructureV2> data) {
        var pathToPosition = getChildWithElement(position);

        if (pathToPosition == position) {
            return doInsertBefore(position, data);
        }

        var selfCopy = copyStructure();
        var iterator = childListIterator();

        while (iterator.hasNext()) {
            var currentChild = iterator.next();
            if (currentChild == pathToPosition) {
                var first = true;
                while (data.hasNext()) {
                    var currentData = data.next();
                    if (first) {
                        var mergedChild = currentChild.splitChildsBefore(position, currentData.childListIterator());
                        mergedChild.getNewStructures().forEach(v -> v.setParentStructure(selfCopy));
                        selfCopy.children.addAll(mergedChild.getNewStructures());
                        first = false;
                    } else {
                        selfCopy.children.add(currentData);
                    }
                }
            } else {
                selfCopy.children.add(currentChild);
            }
        }

        return new TextAddResponseV2(//
                getParentStructure(), //
                this, //
                List.of(selfCopy) //
        );
    }

}
