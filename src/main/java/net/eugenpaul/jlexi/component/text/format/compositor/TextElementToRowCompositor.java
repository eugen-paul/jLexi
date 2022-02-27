package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneRow;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.utils.Size;

public class TextElementToRowCompositor<T extends TextElement> implements TextCompositor<T> {

    @Override
    public List<TextStructureForm> compose(Iterator<T> iterator, Size maxSize) {
        List<TextStructureForm> responseRows = new LinkedList<>();

        TextStructureForm row = new TextPaneRow(null);
        int currentLength = 0;

        while (iterator.hasNext()) {
            T element = iterator.next();

            if (currentLength + element.getSize().getWidth() <= maxSize.getWidth()) {
                row.add(element);
                currentLength += element.getSize().getWidth();
            } else {
                responseRows.add(row);
                row = new TextPaneRow(null);
                currentLength = 0;
            }
        }

        if (!row.isEmpty()) {
            responseRows.add(row);
        }

        return responseRows;
    }

}
