package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> iterator, Size maxSize) {
        List<TextRepresentation> responseRows = new LinkedList<>();

        TextPaneColumn column = new TextPaneColumn(null);
        column.setBackground(background);
        int currentHeight = 0;

        while (iterator.hasNext()) {
            TextRepresentation element = iterator.next();
            if (column.isEmpty() || currentHeight + element.getSize().getHeight() <= maxSize.getHeight()) {
                element.setParent(column);
                element.setRelativPosition(new Vector2d(0, currentHeight));
                column.add(element);
                column.setMarginTop(element.getMarginTop());
                column.setMarginBottom(element.getMarginBottom());
                currentHeight += element.getSize().getHeight();
            } else {
                responseRows.add(column);
                element.setParent(column);
                element.setRelativPosition(new Vector2d(0, 0));
                column = new TextPaneColumn(null);
                column.add(element);
                column.setMarginTop(element.getMarginTop());
                column.setMarginBottom(element.getMarginBottom());
                currentHeight = element.getSize().getHeight();
            }
        }

        if (!column.isEmpty()) {
            responseRows.add(column);
        }

        return responseRows;
    }

}
