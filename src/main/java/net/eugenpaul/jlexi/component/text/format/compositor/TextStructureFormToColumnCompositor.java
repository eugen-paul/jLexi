package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumn;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.utils.Size;

public class TextStructureFormToColumnCompositor implements TextCompositor<TextStructureForm> {

    @Override
    public List<TextStructureForm> compose(Iterator<TextStructureForm> iterator, Size maxSize) {
        List<TextStructureForm> responseRows = new LinkedList<>();

        TextPaneColumn column = new TextPaneColumn(null);
        int currentHeight = 0;

        while (iterator.hasNext()) {
            TextStructureForm element = iterator.next();

            if (currentHeight + element.getSize().getHeight() <= maxSize.getHeight()) {
                column.add(element);
                element.setParent(column);
                currentHeight += element.getSize().getHeight();
            } else {
                responseRows.add(column);
                column = new TextPaneColumn(null);
                column.add(element);
                element.setParent(column);
                currentHeight = element.getSize().getHeight();
            }
        }

        if (!column.isEmpty()) {
            responseRows.add(column);
        }

        return responseRows;
    }

}
