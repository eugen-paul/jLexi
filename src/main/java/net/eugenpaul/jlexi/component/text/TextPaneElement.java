package net.eugenpaul.jlexi.component.text;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.effect.TextPaneEffect;
import net.eugenpaul.jlexi.utils.container.NodeList.NodeListElement;

/**
 * Abstract class for all TextPaneElements (CharGlyph, TableGlyph, ImageGlyph) that could be added to
 * Text-Gui-Components (TaxtPane, Label, ...)
 */
public abstract class TextPaneElement extends Glyph {

    protected List<TextPaneEffect> effectsList;

    @Setter
    @Getter
    protected NodeListElement<TextPaneElement> textPaneListElement;

    protected TextPaneElement(Glyph parent, NodeListElement<TextPaneElement> textPaneListElement) {
        super(parent);
        this.textPaneListElement = textPaneListElement;
        this.effectsList = new LinkedList<>();
    }

    public void addEffect(TextPaneEffect effect) {
        effectsList.add(effect);
    }

    public void removeEffect(TextPaneEffect effect) {
        effectsList.remove(effect);
    }

    public abstract NodeListElement<TextPaneElement> getChildOn(Integer mouseX, Integer mouseY);

    /**
     * The function checks if the element can hold the mouse cursor. For example, a table can contain the cursor, so a
     * keyboard event can be handled by the table. An image element, on the other hand, cannot contain a cursor. In this
     * case, the parent element of the image should process the keyboard event.
     * 
     * @return true if the element can hold the mouse cursor.
     */
    public abstract boolean isCursorHoldable();

    public boolean isEndOfLine() {
        return false;
    }

    public boolean isPlaceHolder() {
        return false;
    }

}
