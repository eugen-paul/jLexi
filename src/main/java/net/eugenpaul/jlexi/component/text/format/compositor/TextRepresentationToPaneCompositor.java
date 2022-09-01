package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.representation.TextPanePanel;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;

public class TextRepresentationToPaneCompositor implements TextCompositor<TextRepresentation> {

    @Override
    public List<TextRepresentation> compose(Iterator<TextRepresentation> iterator, Size maxSize) {
        List<TextRepresentation> responseSites = new LinkedList<>();

        TextPanePanel textPanel = new TextPanePanel(null);

        while (iterator.hasNext()) {
            var size = iterator.next();

            textPanel.add(size);
        }

        if (!textPanel.isEmpty()) {
            responseSites.add(textPanel);
        }

        return responseSites;
    }

}
