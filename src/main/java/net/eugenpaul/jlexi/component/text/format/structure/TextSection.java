package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.iterator.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToColumnCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToSiteCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public class TextSection extends TextStructureOfStructure implements GlyphIterable<TextRepresentation> {

    private TextCompositor<TextRepresentation> compositor;

    private final TextSectionConfiguration configuration;

    private int sitePaddingLeft = 20;
    private int sitePaddingRight = 20;
    private int sitePaddingTop = 40;
    private int sitePaddingBottom = 40;
    private int columnSpacing = 10;

    private final Size siteDrawSize;

    private boolean needRestruct = true;

    public TextSection(TextStructure parentStructure, TextSectionConfiguration configuration) {
        super(parentStructure);

        this.configuration = configuration;

        siteDrawSize = new Size(//
                (configuration.getSiteWidthPx() - sitePaddingLeft - sitePaddingRight)
                        / configuration.getNumberOfColumns(), //
                configuration.getSiteHeightPx() - sitePaddingTop - sitePaddingBottom //
        );

        int columnWidth = (configuration.getSiteWidthPx() - sitePaddingLeft - sitePaddingRight
                - (configuration.getNumberOfColumns() - 1) * columnSpacing) / configuration.getNumberOfColumns();

        this.compositor = new TextRepresentationToSiteCompositor(//
                new Size(configuration.getSiteWidthPx(), configuration.getSiteHeightPx()), //
                configuration.getNumberOfColumns(), //
                columnWidth, //
                columnSpacing, //
                sitePaddingLeft, //
                sitePaddingTop, //
                Color.GREEN //
        );
    }

    @Override
    public Iterator<TextRepresentation> drawableChildIterator() {
        if (null == representation) {
            return Collections.emptyIterator();
        }
        return new ListOfListIterator<>(representation);
    }

    @Override
    public List<TextRepresentation> getRepresentation(Size size) {
        if (null == representation) {
            var columnCompositor = new TextRepresentationToColumnCompositor(Color.WHITE, 0, 0);

            var allRows = new LinkedList<TextRepresentation>();
            for (var paragraph : children) {
                allRows.addAll(paragraph.getRepresentation(siteDrawSize));
            }

            var columns = columnCompositor.compose(allRows.iterator(), siteDrawSize);

            representation = compositor.compose(columns.iterator(), size);
        }
        return representation;
    }

    @Override
    protected boolean checkMergeWith(TextStructure element) {
        return element instanceof TextSection;
    }

    @Override
    protected TextRemoveResponse mergeWith(TextStructure element) {
        if (!checkMergeWith(element)) {
            return TextRemoveResponse.EMPTY;
        }

        var nextSection = (TextSection) element;

        var firstStructure = getLastChild();
        var secondStructure = nextSection.getFirstChild();

        var removedData = firstStructure.mergeWith(secondStructure);
        if (removedData == TextRemoveResponse.EMPTY) {
            // Structures cann't be merged
            return TextRemoveResponse.EMPTY;
        }

        var responseSection = new TextSection(parentStructure, configuration);

        // take over own child elements except the last
        var iteratorFirst = childListIterator();
        while (iteratorFirst.hasNext()) {
            responseSection.children.add(iteratorFirst.next());
        }

        responseSection.children.removeLast();

        //add new element created by mergeWith
        responseSection.children.add(removedData.getNewStructures().get(removedData.getNewStructures().size() - 1));

        // take over child elements from following structure except the first
        var iteratorSecond = nextSection.childListIterator(1);
        while (iteratorSecond.hasNext()) {
            responseSection.children.add(iteratorSecond.next());
        }

        responseSection.children.stream().forEach(v -> v.setParentStructure(responseSection));

        var removedStructures = new LinkedList<List<TextStructure>>();
        removedStructures.addAll(removedData.getRemovedStructures());
        removedStructures.add(List.of(this, nextSection));

        var createdStructures = new LinkedList<TextStructure>();
        createdStructures.addAll(removedData.getNewStructures());
        createdStructures.add(responseSection);

        return new TextRemoveResponse(//
                removedData.getRemovedElement(), //
                removedData.getNewCursorPosition(), //
                removedStructures, //
                createdStructures //
        );
    }

    @Override
    protected TextRemoveResponse mergeChildsWithNext(TextStructure child) {
        var nextChild = getNextChild(child);

        if (nextChild.isPresent()) {
            var removedData = child.mergeWith(nextChild.get());
            if (removedData != TextRemoveResponse.EMPTY) {
                var iterator = this.children.listIterator();
                while (iterator.hasNext()) {
                    // TODO do it better
                    var currentChild = iterator.next();
                    if (currentChild == child) {
                        iterator.remove();
                        iterator.next();
                        iterator.remove();
                        var newStructure = removedData.getNewStructures()
                                .get(removedData.getNewStructures().size() - 1);
                        iterator.add(newStructure);
                        newStructure.setParentStructure(this);
                        break;
                    }
                }
            }

            notifyChangeDown();
            notifyChangeUp();

            return removedData;
        } else if (this.parentStructure != null) {
            return this.parentStructure.mergeChildsWithNext(this);
        }
        return TextRemoveResponse.EMPTY;
    }

    @Override
    protected void restructChildren() {
        super.restructChildren();

        checkRestruct();

        if (!needRestruct) {
            return;
        }

        checkAndSplit();

        needRestruct = false;
    }

    private void checkAndSplit() {
        var iterator = this.children.listIterator();
        var newSection = new TextSection(this.parentStructure, this.configuration.copy());

        clearSplitter();

        boolean doSplit = false;

        while (iterator.hasNext()) {
            var currentElement = (TextParagraph) iterator.next();

            if (doSplit) {
                newSection.add(currentElement);
                currentElement.setParentStructure(newSection);
                iterator.remove();
            }

            if (currentElement.isEndOfSection()) {
                if (!newSection.isEmpty()) {
                    splits.add(newSection);
                }
                newSection = new TextSection(this.parentStructure, this.configuration.copy());
                doSplit = true;
            }
        }
        if (!newSection.isEmpty()) {
            splits.add(newSection);
        }
    }

    public void add(TextParagraph element) {
        this.children.add(element);
        element.setParentStructure(this);

        setRestructIfNeeded(element);
    }

    @Override
    public boolean addBefore(TextElement position, TextElement element) {
        var response = super.addBefore(position, element);
        if (response) {
            setRestructIfNeeded(element);
        }
        return response;
    }

    private void setRestructIfNeeded(TextElement addedElement) {
        if (addedElement.isEndOfSection()) {
            needRestruct = true;
        }
    }

    private void setRestructIfNeeded(TextParagraph addedElement) {
        if (addedElement.isEndOfSection()) {
            needRestruct = true;
        }
    }

    private void checkRestruct() {
        needRestruct = children.stream()//
                .anyMatch(v -> ((TextParagraph) v).isEndOfSection());
    }

    @Override
    public void clear() {
        this.children.clear();
        this.representation = null;
    }

    @Override
    public boolean isEmpty() {
        return children.isEmpty();
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
