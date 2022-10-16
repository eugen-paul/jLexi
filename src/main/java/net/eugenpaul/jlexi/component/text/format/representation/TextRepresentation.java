package net.eugenpaul.jlexi.component.text.format.representation;

import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextRepresentation extends Glyph implements GlyphIterable<TextRepresentation> {

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

    protected TextFieldType fieldType;

    protected TextRepresentation(Glyph parent) {
        super(parent);

        this.fieldType = TextFieldType.UNKNOWN;

        this.marginLeft = 0;
        this.marginRight = 0;
        this.marginTop = 0;
        this.marginBottom = 0;
    }

    public abstract boolean isEmpty();

    public abstract TextPosition getCursorElementAt(Vector2d pos);

    public TextPosition move(TextRepresentation fromChild, MovePosition moving, TextFieldType fieldType, int xOffset) {
        switch (moving) {
        case UP:
            return moveUp(fromChild, fieldType, xOffset);
        case DOWN:
            return moveDown(fromChild, fieldType, xOffset);
        case NEXT:
            return moveNext(fromChild, fieldType, xOffset);
        case PREVIOUS:
            return movePrevious(fromChild, fieldType, xOffset);
        case BENIG_OF_LINE:
            return moveFirst(fromChild, fieldType, xOffset);
        case END_OF_LINE:
            return moveLast(fromChild, fieldType, xOffset);
        default:
            throw new NotYetImplementedException("Moving " + moving + " is not implemented.");
        }
    }

    protected abstract TextPosition moveIn(MovePosition moving, TextFieldType fieldType, int xOffset);

    protected abstract TextPosition moveUp(TextRepresentation fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPosition moveDown(TextRepresentation fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPosition moveNext(TextRepresentation fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPosition movePrevious(TextRepresentation fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPosition moveFirst(TextRepresentation fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPosition moveLast(TextRepresentation fromChild, TextFieldType fieldType, int xOffset);

    public abstract TextPosition getFirstChild();

    public abstract TextPosition getLastChild();

    public abstract boolean isChild(TextRepresentation representation);

    public abstract TextRepresentation getNextRepresentation(TextRepresentation representation);

    public abstract TextRepresentation getPreviousRepresentation(TextRepresentation structure);

    protected abstract TextPosition getLastText(int x);

    protected abstract TextPosition getFirstText(int x);

    protected TextRepresentation getChildRepresentation(TextPosition position) {
        var childGlyph = position.getTextElement().getChild(this);
        if (!(childGlyph instanceof TextRepresentation)) {
            return null;
        }

        return (TextRepresentation) childGlyph;
    }
}
