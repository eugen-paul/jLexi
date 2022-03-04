package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumn;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextStructureFormToColumnCompositor implements TextCompositor<TextStructureForm> {

    @Override
    public List<TextStructureForm> compose(Iterator<TextStructureForm> iterator, Size maxSize) {
        List<TextStructureForm> responseRows = new LinkedList<>();

        TextStructureForm column = new TextPaneColumn(null);
        int currentHeight = 0;

        Vector2d columnSize = new Vector2d(0, 0);

        while (iterator.hasNext()) {
            TextStructureForm element = iterator.next();

            if (currentHeight + element.getSize().getHeight() <= maxSize.getHeight()) {
                column.add(element);
                element.setParent(column);
                columnSize.setX(Math.max(columnSize.getX(), element.getSize().getWidth()));
                columnSize.setY(columnSize.getY() + element.getSize().getHeight());
                currentHeight += element.getSize().getHeight();
            } else {
                responseRows.add(column);
                column.setSize(columnSize.toSize());
                column = new TextPaneColumn(null);
                column.add(element);
                element.setParent(column);
                columnSize.setX(element.getSize().getWidth());
                columnSize.setY(element.getSize().getHeight());
                currentHeight = element.getSize().getHeight();
            }
        }

        if (!column.isEmpty()) {
            column.setSize(columnSize.toSize());
            responseRows.add(column);
        }

        return responseRows;
    }

}
