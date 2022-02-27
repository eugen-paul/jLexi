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

        TextStructureForm row = new TextPaneColumn(null);
        int currentHeight = 0;

        while (iterator.hasNext()) {
            TextStructureForm element = iterator.next();

            if (currentHeight + element.getSize().getHeight() <= maxSize.getHeight()) {
                row.add(element);
                currentHeight += element.getSize().getHeight();
            } else {
                responseRows.add(row);
                row = new TextPaneColumn(null);
                currentHeight = 0;
            }
        }

        if (!row.isEmpty()) {
            responseRows.add(row);
        }

        return responseRows;
    }

}
