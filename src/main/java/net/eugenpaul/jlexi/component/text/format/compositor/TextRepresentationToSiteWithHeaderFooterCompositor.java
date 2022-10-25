package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumn;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneSite;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeader;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderCreater;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextRepresentationToSiteWithHeaderFooterCompositor implements TextCompositor<TextRepresentation> {

    @Getter
    private final Color background;

    private final Size fullPageSize;
    private final int columnPerSite;

    private final int columnSpacing;
    private final int paddingLeft;
    private final int paddingTop;
    private final int paddingBottom;

    private final int columnWidth;

    // TODO create and add footerCreater
    private final TextHeaderCreater header;

    public TextRepresentationToSiteWithHeaderFooterCompositor(Size fullPageSize, int columnPerSite, int columnWidth,
            int columnSpacing, int paddingLeft, int paddingTop, int paddingBottom, Color background,
            TextHeaderCreater header) {
        this.fullPageSize = fullPageSize;
        this.columnPerSite = columnPerSite;
        this.columnSpacing = columnSpacing;
        this.columnWidth = columnWidth;
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingBottom = paddingBottom;
        this.background = background;
        this.header = header;
    }

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> textRowsIterator, Size maxSize) {
        // get header and footer size to copmute body size
        int headerH = 0;

        List<TextRepresentation> responseSites = new LinkedList<>();

        while (textRowsIterator.hasNext()) {
            TextHeader currentHeader = null;
            if (this.header != null) {
                currentHeader = this.header.createNext();
                headerH = currentHeader.getRepresentation().stream().mapToInt(v -> v.getSize().getHeight()).sum();
            }

            int maxColumnH = this.fullPageSize.getHeight() - paddingTop - paddingBottom - headerH;

            TextPaneSite site = new TextPaneSite(null, this.fullPageSize);

            if (currentHeader != null) {
                currentHeader.getRepresentation().forEach(site::add);
            }

            for (int column = 0; column < columnPerSite; column++) {
                var currentTextColumn = computeColumn(textRowsIterator, maxColumnH);
                site.add(currentTextColumn);
                currentTextColumn.setRelativPosition(computePosition(column));
            }

            responseSites.add(site);
        }

        return responseSites;
    }

    private TextPaneColumn computeColumn(Iterator<TextRepresentation> textRowsIterator, int maxColumnH) {
        TextPaneColumn currentTextColumn = createColumn();
        int currentHeight = 0;
        while (textRowsIterator.hasNext()) {
            var row = textRowsIterator.next();
            currentHeight += row.getMarginTop();

            if (currentTextColumn.isEmpty() || currentHeight + row.getSize().getHeight() <= maxColumnH) {
                currentHeight = addToColumn(currentTextColumn, currentHeight, row);
            } else {
                break;
            }
        }
        return currentTextColumn;
    }

    private Vector2d computePosition(int column) {
        return new Vector2d( //
                this.paddingLeft + (column + this.columnWidth + this.columnSpacing) * column, //
                this.paddingTop //
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
