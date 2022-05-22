package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumn;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.AligmentH;
import net.eugenpaul.jlexi.utils.Color;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

/**
 * Put all elements to one column. Set horizontal alignment of the all elements in the column.
 * 
 * @param <T>
 */
@AllArgsConstructor
public class HorizontalAlignmentRepresentationCompositor implements TextCompositor<TextRepresentation> {

    @Getter
    @Setter
    private Color backgroundColor;

    @Getter
    @Setter
    private AligmentH aligment;

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> iterator, Size maxSize) {
        TextPaneColumn responseColumn = new TextPaneColumn(null);
        responseColumn.setBackground(backgroundColor);

        List<TextRepresentation> listOfElements = new LinkedList<>();

        int maxWidth = 0;
        while (iterator.hasNext()) {
            var element = iterator.next();
            listOfElements.add(element);
            maxWidth = Math.max(maxWidth, element.getSize().getWidth());
        }

        int currentY = 0;
        for (var element : listOfElements) {
            currentY += element.getMarginTop();
            element.setParent(responseColumn);
            switch (aligment) {
            case CENTER_POSITIV:
                element.setRelativPosition(//
                        new Vector2d(//
                                Math.max(0, maxSize.getWidth() / 2 - maxWidth / 2), //
                                Math.max(0, currentY) //
                        ) //
                );
                break;
            case CENTER:
                element.setRelativPosition(new Vector2d(maxSize.getWidth() / 2 - maxWidth / 2, currentY));
                break;
            case LEFT:
                element.setRelativPosition(new Vector2d(0, currentY));
                break;
            case RIGHT:
                element.setRelativPosition(new Vector2d(maxWidth - element.getSize().getWidth(), currentY));
                break;
            default:
                element.setRelativPosition(new Vector2d(0, currentY));
                break;
            }
            responseColumn.add(element);
            currentY += element.getSize().getHeight() + element.getMarginBottom();
        }

        return List.of(responseColumn);
    }

}
