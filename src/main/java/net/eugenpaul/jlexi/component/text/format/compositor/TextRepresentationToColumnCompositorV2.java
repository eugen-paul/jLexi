package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumnV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@AllArgsConstructor
public class TextRepresentationToColumnCompositorV2 implements TextCompositorV2<TextRepresentationV2> {

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
    public List<TextRepresentationV2> compose(ListIterator<TextRepresentationV2> iterator, Size maxSize) {
        List<TextRepresentationV2> responseColumns = new LinkedList<>();

        TextPaneColumnV2 column = createColumn();
        int currentHeight = 0;

        while (iterator.hasNext()) {
            TextRepresentationV2 element = iterator.next();
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

    private int addToColumn(TextPaneColumnV2 column, int currentHeight, TextRepresentationV2 element) {
        element.setRelativPosition(new Vector2d(0, currentHeight));
        column.add(element);
        currentHeight += element.getSize().getHeight() + element.getMarginBottom();
        return currentHeight;
    }

    private TextPaneColumnV2 createColumn() {
        TextPaneColumnV2 column = new TextPaneColumnV2(null);
        column.setBackground(this.background);
        column.setMarginTop(this.marginTop);
        column.setMarginBottom(this.marginBottom);
        return column;
    }

}
