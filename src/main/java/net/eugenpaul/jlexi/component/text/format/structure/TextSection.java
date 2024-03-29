package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.iterator.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToColumnCompositor;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToPageCompositor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public class TextSection extends TextStructureOfStructure implements GlyphIterable<TextRepresentation> {

    private TextCompositor<TextRepresentation> compositor;

    private final TextSectionConfiguration configuration;

    private int pagePaddingLeft = 20;
    private int pagePaddingRight = 20;
    private int pagePaddingTop = 40;
    private int pagePaddingBottom = 40;
    private int columnSpacing = 10;

    private TextHeaderCreater headerCreater;
    private TextFooterCreater footerCreater;

    public TextSection(TextStructure parentStructure, TextSectionConfiguration configuration) {
        super(parentStructure);

        this.configuration = configuration;

        if (!this.configuration.isBlock()) {
            this.compositor = new TextRepresentationToPageCompositor(//
                    new Size(configuration.getPageWidthPx(), configuration.getPageHeightPx()), //
                    configuration.getNumberOfColumns(), //
                    computeColumnWidth(), //
                    columnSpacing, //
                    pagePaddingLeft, //
                    pagePaddingTop, //
                    pagePaddingBottom, //
                    Color.INVISIBLE, //
                    headerCreater, //
                    footerCreater //
            );
        } else {
            this.compositor = new TextRepresentationToColumnCompositor(//
                    Color.INVISIBLE, //
                    0, //
                    0 //
            );
        }

        this.headerCreater = null;
    }

    public void setHeaderCreater(TextHeaderCreater headerCreater) {
        setHeaderFooterCreater(headerCreater, this.footerCreater);
    }

    public void setFooterCreater(TextFooterCreater footerCreater) {
        setHeaderFooterCreater(this.headerCreater, footerCreater);
    }

    private void setHeaderFooterCreater(TextHeaderCreater headerCreater, TextFooterCreater footerCreater) {
        if (this.configuration.isBlock()) {
            return;
        }

        this.headerCreater = headerCreater;
        this.footerCreater = footerCreater;

        this.compositor = new TextRepresentationToPageCompositor(//
                new Size(configuration.getPageWidthPx(), configuration.getPageHeightPx()), //
                configuration.getNumberOfColumns(), //
                computeColumnWidth(), //
                columnSpacing, //
                pagePaddingLeft, //
                pagePaddingTop, //
                pagePaddingBottom, //
                Color.INVISIBLE, //
                this.headerCreater, //
                this.footerCreater //
        );
    }

    private int computeColumnWidth() {
        return (configuration.getPageWidthPx() - pagePaddingLeft - pagePaddingRight
                - (configuration.getNumberOfColumns() - 1) * columnSpacing) / configuration.getNumberOfColumns();
    }

    @Override
    public Iterator<TextRepresentation> drawableChildIterator() {
        if (null == getRepresentation()) {
            return Collections.emptyIterator();
        }
        return new ListOfListIterator<>(getRepresentation());
    }

    @Override
    public List<TextRepresentation> getRepresentation(Size size) {
        setRepresentation(null);

        var allRows = new LinkedList<TextRepresentation>();
        var pageSize = new Size(//
                this.configuration.getPageWidthPx(), //
                this.configuration.getPageHeightPx() //
        );

        var rowSize = new Size(//
                (configuration.getPageWidthPx() - pagePaddingLeft - pagePaddingRight)
                        / configuration.getNumberOfColumns(), //
                this.configuration.getPageHeightPx() //
        );

        for (var paragraph : this.children) {
            allRows.addAll(paragraph.getRepresentation(rowSize));
        }

        setRepresentation(this.compositor.compose(allRows.listIterator(), pageSize));

        return getRepresentation();
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

        // By merging of two Section we must remove the endOfSection-Element at the end of first section. To do this, we
        // must merge the last child with the first child of incomming element.
        var firstStructure = getLastChild();
        var secondStructure = nextSection.getFirstChild();

        var removedData = firstStructure.mergeWith(secondStructure);
        if (removedData == TextRemoveResponse.EMPTY) {
            // Structures cann't be merged
            return TextRemoveResponse.EMPTY;
        }

        var responseSection = new TextSection(getParentStructure(), configuration);
        responseSection.setHeaderCreater(headerCreater);
        responseSection.setFooterCreater(footerCreater);

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

        return new TextRemoveResponse(//
                removedData.getRemovedElement(), //
                removedData.getNewCursorPosition(), //
                this, //
                List.of(this, nextSection), //
                List.of(responseSection) //
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

            return new TextRemoveResponse(//
                    removedData.getRemovedElement(), //
                    removedData.getNewCursorPosition(), //
                    this, //
                    removedData.getRemovedStructures(), //
                    removedData.getNewStructures() //
            );
        } else if (getParentStructure() != null) {
            return getParentStructure().mergeChildsWithNext(this);
        }
        return TextRemoveResponse.EMPTY;
    }

    @Override
    // TODO replace this Function with replaceAndSplit
    public TextAddResponse splitChild(TextStructure child, List<TextStructure> to) {

        if (to.get(0).getLastElement().isEndOfSection() && getParentStructure() != null) {
            var splitResult = replaceAndSplit(child, to);
            return getParentStructure().splitChild(this, splitResult);
        }

        var chiltIterator = this.children.listIterator();
        while (chiltIterator.hasNext()) {
            var elem = chiltIterator.next();
            if (elem == child) {
                chiltIterator.remove();
                to.forEach(chiltIterator::add);
                to.forEach(v -> v.setParentStructure(this));

                notifyChangeUp();

                return new TextAddResponse(//
                        this, //
                        child, //
                        to //
                );
            }
        }

        throw new IllegalArgumentException("Cann't split section. Child to replace not found.");
    }

    private List<TextStructure> replaceAndSplit(TextStructure position, List<TextStructure> to) {
        var first = new TextSection(getParentStructure(), this.configuration);
        first.setHeaderCreater(headerCreater);
        first.setFooterCreater(footerCreater);

        var second = new TextSection(getParentStructure(), this.configuration);
        second.setHeaderCreater(headerCreater);
        second.setFooterCreater(footerCreater);

        var current = first;

        var chiltIterator = this.children.listIterator();
        while (chiltIterator.hasNext()) {
            var currentElement = chiltIterator.next();
            if (currentElement == position) {
                current.children.add(to.get(0));
                to.get(0).setParentStructure(current);
                current = second;
                current.children.add(to.get(1));
                to.get(1).setParentStructure(current);
            } else {
                current.children.add(currentElement);
                currentElement.setParentStructure(current);
            }
        }

        return List.of(first, second);
    }

    public void add(TextParagraph element) {
        this.children.add(element);
        element.setParentStructure(this);
    }

    @Override
    public void clear() {
        this.children.clear();
        setRepresentation(null);
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
