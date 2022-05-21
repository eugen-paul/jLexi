package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.element.TextChar;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneElementRow;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@AllArgsConstructor
public class TextElementToRowCompositor<T extends TextElement> implements TextCompositor<T> {

    @Getter
    @Setter
    private int rowMarginTop;

    @Getter
    @Setter
    private int rowMarginBottom;

    @Override
    public List<TextRepresentation> compose(Iterator<T> iterator, Size maxSize) {
        List<TextRepresentation> responseRows = new LinkedList<>();
        List<TextElement> elementsToRow = new LinkedList<>();

        int currentLength = 0;
        int currentHeight = 0;

        while (iterator.hasNext()) {
            T element = iterator.next();

            // TODO get and use margin of elements

            if (currentLength + element.getSize().getWidth() <= maxSize.getWidth() || elementsToRow.isEmpty()) {
                elementsToRow.add(element);
                currentLength += element.getSize().getWidth();
                currentHeight = Math.max(currentHeight, element.getSize().getHeight());
            } else {
                setRelativPositions(elementsToRow, currentHeight);
                TextPaneElementRow row = createRow(elementsToRow);
                responseRows.add(row);

                elementsToRow.clear();
                elementsToRow.add(element);
                currentLength = element.getSize().getWidth();
                currentHeight = element.getSize().getHeight();
            }
        }

        if (!elementsToRow.isEmpty()) {
            setRelativPositions(elementsToRow, currentHeight);
            TextPaneElementRow row = createRow(elementsToRow);
            responseRows.add(row);
        }

        return responseRows;
    }

    private TextPaneElementRow createRow(List<TextElement> elementsToRow) {
        TextPaneElementRow row = new TextPaneElementRow(null, elementsToRow);
        row.setMarginTop(rowMarginTop);
        row.setMarginBottom(rowMarginBottom);
        return row;
    }

    private void setRelativPositions(List<TextElement> elementsToRow, int maxHeight) {
        int currentX = 0;
        for (TextElement element : elementsToRow) {
            if (element instanceof TextChar) {
                var charElement = (TextChar) element;
                int d = charElement.getDescent();
                element.setRelativPosition(new Vector2d(//
                        currentX, //
                        Math.max(0, maxHeight - element.getSize().getHeight() - d)//
                ));
            } else {
                element.setRelativPosition(new Vector2d(//
                        currentX, //
                        maxHeight - element.getSize().getHeight() //
                ));
            }
            currentX += element.getSize().getWidth();
        }
    }

}
