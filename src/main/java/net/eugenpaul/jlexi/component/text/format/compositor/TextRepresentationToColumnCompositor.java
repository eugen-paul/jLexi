package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumn;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextRepresentationToColumnCompositor implements TextCompositor<TextRepresentation> {

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> iterator, Size maxSize) {
        List<TextRepresentation> responseRows = new LinkedList<>();

        TextPaneColumn column = new TextPaneColumn(null);
        int currentHeight = 0;

        while (iterator.hasNext()) {
            TextRepresentation element = iterator.next();

            if (column.isEmpty() || currentHeight + element.getSize().getHeight() <= maxSize.getHeight()) {
                element.setParent(column);
                element.setRelativPosition(new Vector2d(0, currentHeight));
                column.add(element);
                currentHeight += element.getSize().getHeight();
            } else {
                responseRows.add(column);
                element.setParent(column);
                element.setRelativPosition(new Vector2d(0, 0));
                column = new TextPaneColumn(null);
                column.add(element);
                currentHeight = element.getSize().getHeight();
            }
        }

        if (!column.isEmpty()) {
            responseRows.add(column);
        }

        return responseRows;
    }

}
