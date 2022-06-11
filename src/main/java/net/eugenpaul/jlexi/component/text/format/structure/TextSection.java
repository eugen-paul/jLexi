package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.var;
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
    protected TextElement mergeWithNext(TextStructure element) {
        if (!checkMergeWith(element)) {
            return null;
        }

        TextSection nextSection = (TextSection) element;

        TextStructure a = children.getLast();
        TextStructure b = nextSection.children.getFirst();

        nextSection.children.stream().forEach(v -> v.setParentStructure(this));
        children.addAll(nextSection.children);

        TextElement responseSeparator = a.mergeWithNext(b);

        children.remove(b);

        representation = null;

        return responseSeparator;
    }

    @Override
    protected TextElement mergeWithPrevious(TextStructure element) {
        if (!checkMergeWith(element)) {
            return null;
        }

        TextSection previousParagraph = (TextSection) element;

        previousParagraph.children.stream().forEach(v -> v.setParentStructure(this));

        TextElement position = children.getFirst().getFirstElement();

        // After merging two sections, check whether you need to merge the last paragraph of section A with the first
        // paragraph of section B.
        TextStructure a = previousParagraph.children.getLast();
        TextStructure b = children.getFirst();

        children.addAll(0, previousParagraph.children);

        TextElement positionAfterInnerMerge = b.mergeWithPrevious(a);

        if (positionAfterInnerMerge != null) {
            children.remove(a);
            position = positionAfterInnerMerge;
        }

        representation = null;

        return position;
    }

    @Override
    public void resetStructure() {
        representation = null;
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
        var newSection = new TextSection(this.parentStructure, this.configuration);

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
                newSection = new TextSection(this.parentStructure, this.configuration);
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
        children.clear();
        resetStructure();
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
