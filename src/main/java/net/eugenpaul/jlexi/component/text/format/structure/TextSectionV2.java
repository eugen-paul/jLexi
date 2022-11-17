package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.iterator.ListOfListIterator;
import net.eugenpaul.jlexi.component.text.format.compositor.TextCompositorV2;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToColumnCompositorV2;
import net.eugenpaul.jlexi.component.text.format.compositor.TextRepresentationToPageCompositorV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;

public class TextSectionV2 extends TextStructureOfStructureV2 implements GlyphIterable<TextRepresentationV2> {

    private TextCompositorV2<TextRepresentationV2> compositor;

    private final TextSectionConfiguration configuration;

    private int pagePaddingLeft = 20;
    private int pagePaddingRight = 20;
    private int pagePaddingTop = 40;
    private int pagePaddingBottom = 40;
    private int columnSpacing = 10;

    private TextHeaderCreaterV2 headerCreater;
    private TextFooterCreaterV2 footerCreater;

    public TextSectionV2(TextStructureV2 parentStructure, TextSectionConfiguration configuration) {
        super(parentStructure);

        this.configuration = configuration;

        if (!this.configuration.isBlock()) {
            this.compositor = new TextRepresentationToPageCompositorV2(//
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
            this.compositor = new TextRepresentationToColumnCompositorV2(//
                    Color.INVISIBLE, //
                    0, //
                    0 //
            );
        }

        this.headerCreater = null;
    }

    public void setHeaderCreater(TextHeaderCreaterV2 headerCreater) {
        setHeaderFooterCreater(headerCreater, this.footerCreater);
    }

    public void setFooterCreater(TextFooterCreaterV2 footerCreater) {
        setHeaderFooterCreater(this.headerCreater, footerCreater);
    }

    private void setHeaderFooterCreater(TextHeaderCreaterV2 headerCreater, TextFooterCreaterV2 footerCreater) {
        if (this.configuration.isBlock()) {
            return;
        }

        this.headerCreater = headerCreater;
        this.footerCreater = footerCreater;

        this.compositor = new TextRepresentationToPageCompositorV2(//
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
    public Iterator<TextRepresentationV2> drawableChildIterator() {
        if (null == getRepresentation()) {
            return Collections.emptyIterator();
        }
        return new ListOfListIterator<>(getRepresentation());
    }

    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        setRepresentation(null);

        var allRows = new LinkedList<TextRepresentationV2>();
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
    protected boolean checkMergeWith(TextStructureV2 element) {
        return element instanceof TextSectionV2;
    }

    @Override
    protected TextStructureOfStructureV2 copyStructure() {
        var responseSection = new TextSectionV2(getParentStructure(), this.configuration);
        responseSection.setHeaderCreater(this.headerCreater);
        responseSection.setFooterCreater(this.footerCreater);
        return responseSection;
    }

    @Override
    protected boolean hasToBeSplited(TextStructureV2 newChild) {
        return newChild.isEndOfSection();
    }

    public void add(TextParagraphV2 element) {
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

}