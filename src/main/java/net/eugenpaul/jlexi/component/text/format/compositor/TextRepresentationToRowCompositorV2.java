package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneRowV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Builder
public class TextRepresentationToRowCompositorV2 implements TextCompositorV2<TextRepresentationV2> {

    @Override
    public List<TextRepresentationV2> compose(ListIterator<TextRepresentationV2> iterator, Size maxSize) {
        List<TextRepresentationV2> responseRows = new LinkedList<>();

        int currentLength = 0;
        int currentHeight = 0;

        boolean isFirstElement = true;

        TextPaneRowV2 row = createRow();

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

    private TextPaneRowV2 createRow() {
        return new TextPaneRowV2(null);
    }

    private int addToRow(TextPaneRowV2 row, int currentLength, TextRepresentationV2 element) {
        element.setRelativPosition(new Vector2d(currentLength, 0));
        row.add(element);
        currentLength += element.getSize().getWidth() + element.getMarginRight();

        row.setMarginTop(Math.max(row.getMarginTop(), element.getMarginTop()));
        row.setMarginBottom(Math.max(row.getMarginBottom(), element.getMarginBottom()));

        return currentLength;
    }

}
