package net.eugenpaul.jlexi.component.text.format.representation;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.utils.Vector2d;

public abstract class TextRepresentationV2 extends Glyph implements GlyphIterable<TextRepresentationV2> {

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

    @Getter
    @Setter(value = AccessLevel.PROTECTED)
    private TextFieldType fieldType;

    protected TextRepresentationV2(Glyph parent) {
        super(parent);

        this.fieldType = TextFieldType.UNKNOWN;

        this.marginLeft = 0;
        this.marginRight = 0;
        this.marginTop = 0;
        this.marginBottom = 0;
    }

    public abstract boolean isEmpty();

    public abstract TextPositionV2 getCursorElementAt(Vector2d pos);

    public TextPositionV2 move(TextRepresentationV2 fromChild, MovePosition moving, TextFieldType fieldType, int xOffset) {
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

    protected static final boolean checkMove(TextFieldType fromType, TextFieldType toType) {
        if (fromType == TextFieldType.UNKNOWN || toType == TextFieldType.UNKNOWN) {
            return true;
        }

        return fromType.isSame(toType);
    }

    protected abstract TextPositionV2 moveIn(MovePosition moving, TextFieldType fieldType, int xOffset);

    protected abstract TextPositionV2 moveUp(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPositionV2 moveDown(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPositionV2 moveNext(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPositionV2 movePrevious(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPositionV2 moveFirst(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset);

    protected abstract TextPositionV2 moveLast(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset);

    public abstract TextPositionV2 getFirstChild();

    public abstract TextPositionV2 getLastChild();

    public abstract boolean isChild(TextRepresentationV2 representation);

    public abstract TextRepresentationV2 getNextRepresentation(TextRepresentationV2 representation);

    public abstract TextRepresentationV2 getPreviousRepresentation(TextRepresentationV2 structure);

    protected abstract TextPositionV2 getLastText(int x);

    protected abstract TextPositionV2 getFirstText(int x);

    // protected TextRepresentationV2 getChildRepresentation(TextPositionV2 position) {
    //     var childGlyph = position.getTextElement().getChild(this);
    //     if (!(childGlyph instanceof TextRepresentationV2)) {
    //         return null;
    //     }

    //     return (TextRepresentationV2) childGlyph;
    // }
}
