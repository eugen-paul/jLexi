package net.eugenpaul.jlexi.component.text.keyhandler;

import net.eugenpaul.jlexi.command.TextCommand;
import net.eugenpaul.jlexi.command.TextElementAddBeforeCommand;
import net.eugenpaul.jlexi.command.TextElementRemoveCommant;
import net.eugenpaul.jlexi.command.TextRemoveBevorCommant;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
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
        cursor.removeSelection();

        var addCommand = new TextElementAddBeforeCommand(//
                TextElementFactory.fromChar(//
                        storage, //
                        key, //
                        cursor.getTextFormat(), //
                        cursor.getTextFormatEffect()//
                ), //
                cursor.getPosition());

        doTextCommant(addCommand);
    }

    public void onKeyPressed(KeyCode keyCode) {
        // TODO You may have to edit the selected text here.
        component.getMouseCursor().removeSelection();

        switch (keyCode) {
        case ENTER:
            keyPressedEnter();
            break;
        case RIGHT, LEFT, UP, DOWN:
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

        var addCommand = new TextElementAddBeforeCommand(//
                TextElementFactory.genNewLineChar(//
                        storage, //
                        cursor.getTextFormat(), //
                        cursor.getTextFormatEffect()//
                ), //
                cursor.getPosition());

        doTextCommant(addCommand);
    }

    private void keyPressedBackSpace() {
        var cursor = component.getMouseCursor();

        var deleteCommand = new TextRemoveBevorCommant(cursor.getPosition());
        doTextCommant(deleteCommand);

        cursor.moveCursorTo(deleteCommand.getCursorPosition());
    }

    private void keyPressedDelete() {
        var cursor = component.getMouseCursor();

        var deleteCommand = new TextElementRemoveCommant(cursor.getPosition());
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
            cursorPosition = representation.getNext(cursor.getPosition());
            break;
        case LEFT:
            cursorPosition = representation.getPrevious(cursor.getPosition());
            break;
        case UP:
            cursorPosition = representation.getUp(cursor.getPosition());
            break;
        case DOWN:
            cursorPosition = representation.getDown(cursor.getPosition());
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
