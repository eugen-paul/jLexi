package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneRow;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

public class TextElementToRowCompositor<T extends TextElement> implements TextCompositor<T> {

    @Override
    public List<TextStructureForm> compose(Iterator<T> iterator, Size maxSize) {
        List<TextStructureForm> responseRows = new LinkedList<>();

        TextStructureForm row = new TextPaneRow(null);
        int currentLength = 0;

        Vector2d rowSize = new Vector2d(0, 0);

        while (iterator.hasNext()) {
            T element = iterator.next();

            if (currentLength + element.getSize().getWidth() <= maxSize.getWidth()) {
                row.add(element);
                rowSize.setX(rowSize.getX() + element.getSize().getWidth());
                rowSize.setY(Math.max(rowSize.getY(), element.getSize().getHeight()));
                currentLength += element.getSize().getWidth();
            } else {
                responseRows.add(row);
                row.setSize(rowSize.toSize());
                row = new TextPaneRow(null);
                row.add(element);
                rowSize.setX(element.getSize().getWidth());
                rowSize.setY(element.getSize().getHeight());
                currentLength = 0;
            }
        }

        if (!row.isEmpty()) {
            row.setSize(rowSize.toSize());
            responseRows.add(row);
        }

        return responseRows;
    }

}
