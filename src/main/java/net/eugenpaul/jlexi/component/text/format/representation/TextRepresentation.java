package net.eugenpaul.jlexi.component.text.format.representation;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.CursorControl;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextRepresentation extends Glyph implements CursorControl, GlyphIterable<TextRepresentation> {

    @Getter
    @Setter
    private int marginLeft;
    @Getter
    @Setter
    private int marginRight;
    @Getter
    @Setter
    private int marginTop;
    @Getter
    @Setter
    private int marginBottom;

    protected TextRepresentation(Glyph parent) {
        super(parent);

        this.marginLeft = 0;
        this.marginRight = 0;
        this.marginTop = 0;
        this.marginBottom = 0;
    }

    public abstract boolean isEmpty();

    public abstract TextPosition getCursorElementAt(Vector2d pos);

    public abstract TextPositionV2 getCursorElementAtV2(Vector2d pos);

    public abstract TextPosition getFirstChild();

    public abstract TextPosition getLastChild();

    public abstract TextRepresentation getNextRepresentation(TextRepresentation representation);

    public abstract TextRepresentation getPreviousRepresentation(TextRepresentation structure);

    protected abstract TextPositionV2 getLastTextV2(int x);

    protected abstract TextPositionV2 getFirstTextV2(int x);

    protected TextRepresentation getChildRepresentation(TextPositionV2 position) {
        var childGlyph = position.getTextElement().getChild(this);
        if (!(childGlyph instanceof TextRepresentation)) {
            return null;
        }

        return (TextRepresentation) childGlyph;
    }
}
