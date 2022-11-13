package net.eugenpaul.jlexi.component.text.format.representation;

import java.util.Iterator;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.format.CursorMovingV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.visitor.Visitor;

public class TextPaneElement extends TextRepresentationV2 implements CursorMovingV2 {

    private TextElementV2 dataElement;
    private TextPositionV2 position;

    public TextPaneElement(Glyph parent, TextElementV2 dataElement) {
        super(parent);
        this.dataElement = dataElement;
        this.position = dataElement.getTextPosition();
    }

    @Override
    public TextPositionV2 getCursorElementAt(Vector2d pos) {
        return getFirstChild();
    }

    @Override
    public TextPositionV2 getFirstChild() {
        return position;
    }

    @Override
    public TextPositionV2 getLastChild() {
        return getFirstChild();
    }

    @Override
    public Size getSize() {
        return getDrawable().getSize();
    }

    @Override
    public TextPositionV2 move(TextPositionV2 position, MovePosition moving) {
        if (getParent() instanceof TextRepresentationV2) {
            return ((TextRepresentationV2) getParent()).move(//
                    this, //
                    moving, //
                    getFieldType(), //
                    0 //
            );
        }

        return null;
    }

    @Override
    protected TextPositionV2 moveIn(MovePosition moving, TextFieldType fieldType, int xOffset) {
        if (!checkMove(fieldType, getFieldType())) {
            return null;
        }

        return this.position;
    }

    @Override
    protected TextPositionV2 moveUp(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.UP, fieldType, xOffset);
    }

    @Override
    protected TextPositionV2 moveDown(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.DOWN, fieldType, xOffset);
    }

    @Override
    protected TextPositionV2 moveNext(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.NEXT, fieldType, xOffset);
    }

    @Override
    protected TextPositionV2 movePrevious(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        return moveIn(MovePosition.PREVIOUS, fieldType, xOffset);
    }

    @Override
    protected TextPositionV2 moveFirst(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        return getFirstChild();
    }

    @Override
    protected TextPositionV2 moveLast(TextRepresentationV2 fromChild, TextFieldType fieldType, int xOffset) {
        return getLastChild();
    }

    @Override
    public boolean isChild(TextRepresentationV2 representation) {
        //TODO
        return false;
    }

    @Override
    public Drawable getDrawable() {
        return this.dataElement.getDrawable();
    }

    @Override
    public Iterator<Glyph> iterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void visit(Visitor checker) {
        // TODO Auto-generated method stub

    }

    @Override
    public Iterator<TextRepresentationV2> drawableChildIterator() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TextRepresentationV2 getNextRepresentation(TextRepresentationV2 structure) {
        return null;
    }

    @Override
    public TextRepresentationV2 getPreviousRepresentation(TextRepresentationV2 structure) {
        return null;
    }

    @Override
    protected TextPositionV2 getFirstText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    protected TextPositionV2 getLastText(int x) {
        return getCursorElementAt(new Vector2d(x, 0));
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
