package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.command.TextCommand;
import net.eugenpaul.jlexi.command.TextElementAddBeforeCommand;
import net.eugenpaul.jlexi.command.TextElementRemoveCommant;
import net.eugenpaul.jlexi.command.TextElementRemoveSelectedCommant;
import net.eugenpaul.jlexi.command.TextElementReplaceCommand;
import net.eugenpaul.jlexi.command.TextRemoveBevorCommant;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

public class AbstractKeyHandler {

    private final KeyHandlerable component;
    private final ResourceManager storage;
    private final TextCommandsDeque commandDeque;

    protected AbstractKeyHandler(KeyHandlerable component, ResourceManager storage, TextCommandsDeque commandDeque) {
        this.component = component;
        this.storage = storage;
        this.commandDeque = commandDeque;
    }

    public void onKeyTyped(Character key) {
        if (!CharacterHelper.isPrintable(key)) {
            return;
        }

        var cursor = component.getMouseCursor();

        TextElement element = TextElementFactory.fromChar(//
                storage, //
                key, //
                cursor.getTextFormat(), //
                cursor.getTextFormatEffect()//
        );
        addTextElement(cursor, element);
    }

    public void onKeyPressed(KeyCode keyCode) {
        switch (keyCode) {
        case ENTER:
            keyPressedEnter();
            break;
        case RIGHT, LEFT, UP, DOWN:
            component.getMouseCursor().removeSelection();
            keyPressedCursorMove(keyCode);
            break;
        case DELETE:
            keyPressedDelete();
            break;
        case BACK_SPACE:
            keyPressedBackSpace();
            break;
        case CTRL:
            break;
        case F1:
            undo();
            break;
        case F2:
            redo();
            break;
        default:
            break;
        }
    }

    public void undo() {
        var cursorPosition = commandDeque.undo();

        var cursor = component.getMouseCursor();
        cursor.moveCursorTo(cursorPosition);

        component.getTextRepresentation().redraw();
    }

    public void redo() {
        var cursorPosition = commandDeque.redo();

        var cursor = component.getMouseCursor();
        cursor.moveCursorTo(cursorPosition);

        component.getTextRepresentation().redraw();
    }

    private void doTextCommant(TextCommand command) {
        command.execute();

        if (command.isEmpty()) {
            return;
        }

        component.getTextRepresentation().redraw();

        commandDeque.addCommand(command);
    }

    public void onKeyReleased(KeyCode keyCode) {
        // TODO
    }

    private void keyPressedEnter() {
        var cursor = component.getMouseCursor();

        TextElement addedElement = TextElementFactory.genNewLineChar(//
                storage, //
                cursor.getTextFormat(), //
                cursor.getTextFormatEffect()//
        );
        addTextElement(cursor, addedElement);
    }

    private void addTextElement(Cursor cursor, TextElement addedElement) {
        TextCommand addCommand;
        if (cursor.isTextSelected()) {
            addCommand = new TextElementReplaceCommand(//
                    addedElement, //
                    cursor.getSelectedText());
            cursor.removeSelection();
        } else {
            addCommand = new TextElementAddBeforeCommand(//
                    addedElement, //
                    cursor.getPosition());
        }

        doTextCommant(addCommand);

        cursor.moveCursorTo(addCommand.getCursorPosition());
    }

    private void keyPressedBackSpace() {
        var cursor = component.getMouseCursor();

        TextCommand deleteCommand;
        if (cursor.isTextSelected()) {
            deleteCommand = new TextElementRemoveSelectedCommant(cursor.getSelectedText());
            cursor.removeSelection();
        } else {
            deleteCommand = new TextRemoveBevorCommant(cursor.getPosition());
        }

        doTextCommant(deleteCommand);

        cursor.moveCursorTo(deleteCommand.getCursorPosition());
    }

    private void keyPressedDelete() {
        var cursor = component.getMouseCursor();

        TextCommand deleteCommand;
        if (cursor.isTextSelected()) {
            deleteCommand = new TextElementRemoveSelectedCommant(cursor.getSelectedText());
            cursor.removeSelection();
        } else {
            deleteCommand = new TextElementRemoveCommant(cursor.getPosition());
        }

        doTextCommant(deleteCommand);

        cursor.moveCursorTo(deleteCommand.getCursorPosition());

    }

    private boolean keyPressedCursorMove(KeyCode keyCode) {
        var cursor = component.getMouseCursor();
        var representation = component.getTextRepresentation();

        if (null == representation) {
            return false;
        }

        TextPosition cursorPosition = null;
        switch (keyCode) {
        case RIGHT:
            cursorPosition = cursor.getPosition().afterMove(MovePosition.NEXT);
            break;
        case LEFT:
            cursorPosition = cursor.getPosition().afterMove(MovePosition.PREVIOUS);
            break;
        case UP:
            cursorPosition = cursor.getPosition().afterMove(MovePosition.UP);
            break;
        case DOWN:
            cursorPosition = cursor.getPosition().afterMove(MovePosition.DOWN);
            break;
        default:
            break;
        }

        if (cursorPosition == null) {
            return false;
        }

        cursor.moveCursorTo(cursorPosition);
        return true;
    }

}
