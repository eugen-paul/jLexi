package net.eugenpaul.jlexi.component.text.keyhandler;

import java.util.LinkedList;
import java.util.List;

import net.eugenpaul.jlexi.command.TextAddBeforeCommand;
import net.eugenpaul.jlexi.command.TextCommand;
import net.eugenpaul.jlexi.command.TextRemoveCommant;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

public class AbstractKeyHandler {

    private KeyHandlerable component;
    private ResourceManager storage;

    private LinkedList<TextCommand> undoCommands;
    private LinkedList<TextCommand> redoCommands;

    protected AbstractKeyHandler(KeyHandlerable component, ResourceManager storage) {
        this.component = component;
        this.storage = storage;

        this.undoCommands = new LinkedList<>();
        this.redoCommands = new LinkedList<>();
    }

    public void onKeyTyped(Character key) {
        if (!CharacterHelper.isPrintable(key)) {
            return;
        }

        var cursor = component.getMouseCursor();
        var element = cursor.getTextElement();
        var parentStructure = element.getStructureParent();
        if (null == parentStructure) {
            return;
        }

        var addCommand = new TextAddBeforeCommand(//
                List.of(TextElementFactory.fromChar(null, storage, parentStructure, key, cursor.getTextFormat(),
                        element.getFormatEffect())), //
                element);

        doTextCommant(addCommand);
    }

    public void onKeyPressed(KeyCode keyCode) {
        switch (keyCode) {
        case ENTER:
            keyPressedEnter();
            break;
        case RIGHT, LEFT, UP, DOWN:
            keyPressedCursorMove(keyCode, true);
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
        var command = undoCommands.pollLast();
        if (null == command) {
            return;
        }

        command.unexecute();
        redoCommands.add(command);

        var cursor = component.getMouseCursor();
        cursor.moveCursorTo(command.getCursorPosition());
    }

    public void redo() {
        var command = redoCommands.pollLast();
        if (null == command) {
            return;
        }

        command.execute();
        undoCommands.add(command);

        var cursor = component.getMouseCursor();
        cursor.moveCursorTo(command.getCursorPosition());
    }

    private void doTextCommant(TextCommand command) {
        command.execute();

        if (command.isEmpty()) {
            return;
        }

        if (command.reversible()) {
            undoCommands.add(command);
        } else {
            undoCommands.clear();
        }

        redoCommands.clear();
    }

    public void onKeyReleased(KeyCode keyCode) {
        //TODO
    }

    private void keyPressedEnter() {
        var cursor = component.getMouseCursor();
        var element = cursor.getTextElement();
        var parentStructure = element.getStructureParent();
        if (null == parentStructure) {
            return;
        }

        var addCommand = new TextAddBeforeCommand(//
                List.of(TextElementFactory.genNewLineChar(null, storage, parentStructure, cursor.getTextFormat(),
                        element.getFormatEffect())), //
                element);

        doTextCommant(addCommand);
    }

    private void keyPressedBackSpace() {
        if (keyPressedCursorMove(KeyCode.LEFT, false)) {
            keyPressedDelete();
        }
    }

    private void keyPressedDelete() {
        var cursor = component.getMouseCursor();
        var element = cursor.getTextElement();

        var deleteCommand = new TextRemoveCommant(List.of(element));
        doTextCommant(deleteCommand);

        cursor.moveCursorTo(deleteCommand.getCursorPosition());
    }

    private boolean keyPressedCursorMove(KeyCode keyCode, boolean moveForDelete) {
        var cursor = component.getMouseCursor();
        var structureForm = component.getTextStructureForm();

        if (null == structureForm) {
            return false;
        }

        TextElement el = null;
        switch (keyCode) {
        case RIGHT:
            el = structureForm.getNext(cursor.getTextElement());
            break;
        case LEFT:
            el = structureForm.getPrevious(cursor.getTextElement(), moveForDelete);
            break;
        case UP:
            el = structureForm.getUp(cursor.getTextElement());
            break;
        case DOWN:
            el = structureForm.getDown(cursor.getTextElement());
            break;
        default:
            break;
        }

        if (el == null) {
            return false;
        }

        cursor.moveCursorTo(el);

        return true;
    }

}
