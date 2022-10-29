package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumn;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneRow;
import net.eugenpaul.jlexi.component.text.format.representation.TextPanePage;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeader;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderCreater;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextRepresentationToPageCompositor implements TextCompositor<TextRepresentation> {

    @Getter
    private final Color background;

    private final Size fullPageSize;
    private final int columnPerPage;

    private final int columnSpacing;
    private final int paddingLeft;
    private final int paddingTop;
    private final int paddingBottom;

    private final int columnWidth;

    // TODO create and add footerCreater
    private final TextHeaderCreater header;

    public TextRepresentationToPageCompositor(Size fullPageSize, int columnPerPage, int columnWidth,
            int columnSpacing, int paddingLeft, int paddingTop, int paddingBottom, Color background,
            TextHeaderCreater header) {
        this.fullPageSize = fullPageSize;
        this.columnPerPage = columnPerPage;
        this.columnSpacing = columnSpacing;
        this.columnWidth = columnWidth;
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.background = background;
        this.header = header;
    }

    @Override
    public List<TextRepresentation> compose(ListIterator<TextRepresentation> textRowsIterator, Size maxSize) {
        // get header and footer size to copmute body size
        int headerH = 0;

        List<TextRepresentation> responsePages = new LinkedList<>();

        while (textRowsIterator.hasNext()) {
            TextHeader currentHeader = null;
            if (this.header != null) {
                currentHeader = this.header.createNext();
                headerH = currentHeader.getRepresentation(maxSize).stream()//
                        .mapToInt(v -> v.getSize().getHeight())//
                        .sum();
            }

            TextPanePage page = new TextPanePage(null, this.fullPageSize);

            if (currentHeader != null) {
                var headerRepresentation = currentHeader.getRepresentation(maxSize).get(0);
                headerRepresentation.setRelativPosition(new Vector2d(this.paddingLeft, 0));
                page.setHeader(headerRepresentation);
            }

            int maxColumnH = this.fullPageSize.getHeight() - paddingTop - paddingBottom - headerH;
            TextPaneRow body = new TextPaneRow(page);
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

    private TextPaneColumn computeColumn(ListIterator<TextRepresentation> textRowsIterator, int maxColumnH) {
        TextPaneColumn currentTextColumn = createColumn();
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

    private int addToColumn(TextPaneColumn column, int currentHeight, TextRepresentation element) {
        element.setRelativPosition(new Vector2d(0, currentHeight));
        column.add(element);
        currentHeight += element.getSize().getHeight() + element.getMarginBottom();
        return currentHeight;
    }

    private TextPaneColumn createColumn() {
        TextPaneColumn column = new TextPaneColumn(null);
        column.setBackground(this.background);
        // TODO add/set margin
        return column;
    }

}
