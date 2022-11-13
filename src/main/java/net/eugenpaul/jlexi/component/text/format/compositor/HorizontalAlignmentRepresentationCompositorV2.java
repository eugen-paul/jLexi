package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneColumnV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
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
public class HorizontalAlignmentRepresentationCompositorV2 implements TextCompositorV2<TextRepresentationV2> {

    @Getter
    @Setter
    private Color backgroundColor;

    @Getter
    @Setter
    private AligmentH aligment;

    @Override
    public List<TextRepresentationV2> compose(ListIterator<TextRepresentationV2> iterator, Size maxSize) {
        TextPaneColumnV2 responseColumn = new TextPaneColumnV2(null);
        responseColumn.setBackground(this.backgroundColor);

        List<TextRepresentationV2> listOfElements = new LinkedList<>();

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
            switch (this.aligment) {
            case CENTER_POSITIV:
                element.setRelativPosition(//
                        new Vector2d(//
                                Math.max(0, maxWidth / 2 - element.getSize().getWidth() / 2), //
                                Math.max(0, currentY) //
                        ) //
                );
                break;
            case CENTER:
                element.setRelativPosition(new Vector2d(maxWidth / 2 - element.getSize().getWidth() / 2, currentY));
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
