package net.eugenpaul.jlexi.component.text.structure;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.TextPaneElement;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

public abstract class TextContainer<T extends TextPaneElement> extends TextPaneElement {

    protected TextContainer(Glyph parent, NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent, textPaneListElement);
    }

    public abstract boolean isEmpty();

    public abstract boolean addIfPossible(T element);
    public abstract void add(T element);

    public abstract void updateSize(Size size);

    public abstract TextPaneElement getElementOnPosition(Vector2d position);

    @Override
    public boolean isEndOfLine() {
        return true;
    }

    @Override
    public boolean isCursorHoldable() {
        return true;
    }

}
