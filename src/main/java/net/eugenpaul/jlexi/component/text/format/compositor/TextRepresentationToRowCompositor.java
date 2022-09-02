package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneRow;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Builder
public class TextRepresentationToRowCompositor implements TextCompositor<TextRepresentation> {

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> iterator, Size maxSize) {
        List<TextRepresentation> responseRows = new LinkedList<>();

        int currentLength = 0;
        int currentHeight = 0;

        boolean isFirstElement = true;

        TextPaneRow row = createRow();

        while (iterator.hasNext()) {
            var element = iterator.next();
            currentLength += element.getMarginLeft();

            if (currentLength + element.getSize().getWidth() <= maxSize.getWidth() || isFirstElement) {
                currentHeight = Math.max(currentHeight, element.getSize().getHeight());
                currentLength = addToRow(row, currentLength, element);

                isFirstElement = false;
            } else {
                responseRows.add(row);
                row = createRow();
                currentHeight = element.getSize().getHeight();
                currentLength = element.getMarginLeft();
                currentLength = addToRow(row, currentLength, element);
            }
        }

        if (!row.isEmpty()) {
            responseRows.add(row);
        }

        return responseRows;
    }

    private TextPaneRow createRow() {
        return new TextPaneRow(null);
    }

    private int addToRow(TextPaneRow row, int currentLength, TextRepresentation element) {
        element.setRelativPosition(new Vector2d(currentLength, 0));
        row.add(element);
        currentLength += element.getSize().getWidth() + element.getMarginRight();

        row.setMarginTop(Math.max(row.getMarginTop(), element.getMarginTop()));
        row.setMarginBottom(Math.max(row.getMarginBottom(), element.getMarginBottom()));

        return currentLength;
    }

}
