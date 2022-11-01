package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumn;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@AllArgsConstructor
public class TextRepresentationToColumnCompositor implements TextCompositor<TextRepresentation> {

    @Getter
    @Setter
    private Color background;

    @Getter
    @Setter
    private int marginTop;

    @Getter
    @Setter
    private int marginBottom;

    @Override
    public List<TextRepresentation> compose(ListIterator<TextRepresentation> iterator, Size maxSize) {
        List<TextRepresentation> responseColumns = new LinkedList<>();

        TextPaneColumn column = createColumn();
        int currentHeight = 0;

        while (iterator.hasNext()) {
            TextRepresentation element = iterator.next();
            currentHeight += element.getMarginTop();

            if (column.isEmpty() || currentHeight + element.getSize().getHeight() <= maxSize.getHeight()) {
                currentHeight = addToColumn(column, currentHeight, element);
            } else {
                responseColumns.add(column);
                column = createColumn();
                currentHeight = element.getMarginTop();

                currentHeight = addToColumn(column, currentHeight, element);
            }
        }

        if (!column.isEmpty()) {
            responseColumns.add(column);
        }

        return responseColumns;
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
        column.setMarginTop(this.marginTop);
        column.setMarginBottom(this.marginBottom);
        return column;
    }

}
