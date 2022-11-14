package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumnV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextPanePageV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneRowV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextFooterCreaterV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderCreaterV2;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextRepresentationToPageCompositorV2 implements TextCompositorV2<TextRepresentationV2> {

    private final Color background;

    private final Size fullPageSize;
    private final int columnPerPage;

    private final int columnSpacing;
    private final int paddingLeft;
    private final int paddingTop;
    private final int paddingBottom;

    private final int columnWidth;

    private final TextHeaderCreaterV2 headerCreater;
    private final TextFooterCreaterV2 footerCreater;

    public TextRepresentationToPageCompositorV2(Size fullPageSize, int columnPerPage, int columnWidth, int columnSpacing,
            int paddingLeft, int paddingTop, int paddingBottom, Color background, TextHeaderCreaterV2 headerCreater,
            TextFooterCreaterV2 footerCreater) {
        this.fullPageSize = fullPageSize;
        this.columnPerPage = columnPerPage;
        this.columnSpacing = columnSpacing;
        this.columnWidth = columnWidth;
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.background = background;
        this.headerCreater = headerCreater;
        this.footerCreater = footerCreater;
    }

    @Override
    public List<TextRepresentationV2> compose(ListIterator<TextRepresentationV2> textRowsIterator, Size pageSize) {

        List<TextRepresentationV2> responsePages = new LinkedList<>();

        while (textRowsIterator.hasNext()) {
            int headerH = 0;
            int footerH = 0;
            TextPanePageV2 page = new TextPanePageV2(null, this.fullPageSize);

            if (this.headerCreater != null) {
                var currentHeader = this.headerCreater.createNext();
                var representations = currentHeader.getRepresentation(pageSize);
                if (representations != null && !representations.isEmpty()) {
                    var headerRepresentation = representations.get(0);
                    headerH = headerRepresentation.getSize().getHeight();
                    headerRepresentation.setRelativPosition(new Vector2d(this.paddingLeft, 0));
                    page.setHeader(headerRepresentation);
                }
            }

            if (this.footerCreater != null) {
                var currentFooter = this.footerCreater.createNext();
                var representations = currentFooter.getRepresentation(pageSize);
                if (representations != null && !representations.isEmpty()) {
                    var footerRepresentation = representations.get(0);
                    footerH = footerRepresentation.getSize().getHeight();
                    footerRepresentation
                            .setRelativPosition(new Vector2d(this.paddingLeft, pageSize.getHeight() - footerH));
                    page.setFooter(footerRepresentation);
                }
            }

            int maxColumnH = this.fullPageSize.getHeight() - paddingTop - paddingBottom - headerH - footerH;
            TextPaneRowV2 body = new TextPaneRowV2(page, false);
            body.setRelativPosition(computeBodyPosition(headerH));
            for (int column = 0; column < columnPerPage; column++) {
                var currentTextColumn = computeColumn(textRowsIterator, maxColumnH);
                currentTextColumn.setRelativPosition(computeColumnPosition(column));
                body.add(currentTextColumn);
            }
            page.setBody(body);

            responsePages.add(page);
        }

        return responsePages;
    }

    private TextPaneColumnV2 computeColumn(ListIterator<TextRepresentationV2> textRowsIterator, int maxColumnH) {
        TextPaneColumnV2 currentTextColumn = createColumn();
        int currentHeight = 0;
        while (textRowsIterator.hasNext()) {
            var row = textRowsIterator.next();
            currentHeight += row.getMarginTop();

            if (currentTextColumn.isEmpty() || currentHeight + row.getSize().getHeight() <= maxColumnH) {
                currentHeight = addToColumn(currentTextColumn, currentHeight, row);
            } else {
                if (textRowsIterator.hasPrevious()) {
                    textRowsIterator.previous();
                }
                break;
            }
        }
        return currentTextColumn;
    }

    private Vector2d computeColumnPosition(int column) {
        return new Vector2d( //
                (column + this.columnWidth + this.columnSpacing) * column, //
                0 //
        );
    }

    private Vector2d computeBodyPosition(int headerH) {
        return new Vector2d( //
                this.paddingLeft, //
                this.paddingTop + headerH//
        );
    }

    private int addToColumn(TextPaneColumnV2 column, int currentHeight, TextRepresentationV2 element) {
        element.setRelativPosition(new Vector2d(0, currentHeight));
        column.add(element);
        currentHeight += element.getSize().getHeight() + element.getMarginBottom();
        return currentHeight;
    }

    private TextPaneColumnV2 createColumn() {
        TextPaneColumnV2 column = new TextPaneColumnV2(null);
        column.setBackground(this.background);
        // TODO add/set margin
        return column;
    }

}
