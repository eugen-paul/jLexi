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
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public class TextSection extends TextStructureOfStructure implements GlyphIterable<TextRepresentation> {

    protected TextCompositor<TextRepresentation> compositor;

    private Size siteSize = new Size(595, 842);
    private int sitePaddingLeft = 20;
    private int sitePaddingRight = 20;
    private int sitePaddingTop = 40;
    private int sitePaddingBottom = 40;
    private int columnSpacing = 10;
    private int columnPerSite = 1;

    private final Size siteDrawSize = new Size(//
            (siteSize.getWidth() - sitePaddingLeft - sitePaddingRight) / columnPerSite, //
            siteSize.getHeight() - sitePaddingTop - sitePaddingBottom //
    );

    private boolean needRestruct = true;

    public TextSection(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        super(parentStructure, format, storage);

        int columnWidth = (siteSize.getWidth() - sitePaddingLeft - sitePaddingRight
                - (columnPerSite - 1) * columnSpacing) / columnPerSite;

        this.compositor = new TextRepresentationToSiteCompositor(//
                siteSize, //
                columnPerSite, //
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
        TextElement responseSeparator = null;
        if (((TextParagraph) children.getLast()).isEndOfSection()) {
            responseSeparator = ((TextParagraph) children.getLast()).removeEndOfSection();
        }

        nextSection.children.stream().forEach(v -> v.setParentStructure(this));
        children.addAll(nextSection.children);

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

        var iterator = children.listIterator();
        previousParagraph.children.stream()//
                .filter(v -> !((TextParagraph) v).isEndOfSection())//
                .forEach(iterator::add);

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

        if (!needRestruct) {
            return;
        }

        checkAndSplit();

        needRestruct = false;
    }

    private void checkAndSplit() {
        var iterator = children.listIterator();
        var newParagraph = new TextSection(parentStructure, format, storage);

        clearSplitter();

        boolean doSplit = false;

        while (iterator.hasNext()) {
            var currentElement = (TextParagraph) iterator.next();

            if (doSplit) {
                newParagraph.add(currentElement);
                currentElement.setParentStructure(newParagraph);
                iterator.remove();
            }

            if (currentElement.isEndOfSection()) {
                if (!newParagraph.isEmpty()) {
                    splits.add(newParagraph);
                }
                newParagraph = new TextSection(parentStructure, format, storage);
                doSplit = true;
            }
        }
    }

    public void add(TextParagraph element) {
        children.add(element);
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
