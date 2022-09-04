package net.eugenpaul.jlexi.component.text.keyhandler;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.command.TextCommand;
import net.eugenpaul.jlexi.command.TextElementAddBeforeCommand;
import net.eugenpaul.jlexi.command.TextElementAddRowTextBeforeCommand;
import net.eugenpaul.jlexi.command.TextElementRemoveCommand;
import net.eugenpaul.jlexi.command.TextElementRemoveSelectedCommand;
import net.eugenpaul.jlexi.command.TextElementReplaceCommand;
import net.eugenpaul.jlexi.command.TextRemoveBevorCommand;
import net.eugenpaul.jlexi.component.text.Cursor;
import net.eugenpaul.jlexi.component.text.converter.ClipboardConverter;
import net.eugenpaul.jlexi.component.text.converter.clipboard.ClipboardConverterImpl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.representation.MovePosition;
import net.eugenpaul.jlexi.component.text.format.representation.TextPosition;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

@Slf4j
public class AbstractKeyHandler {

    private final KeyHandlerable component;
    private final ResourceManager storage;
    private final TextCommandsDeque commandDeque;
    private final ClipboardConverter clipboardConverter;

    // TODO just for test. refactor it
    private boolean ctrlPressed;

    protected AbstractKeyHandler(KeyHandlerable component, ResourceManager storage, TextCommandsDeque commandDeque) {
        this.component = component;
        this.storage = storage;
        this.commandDeque = commandDeque;
        this.clipboardConverter = new ClipboardConverterImpl(storage);
        this.ctrlPressed = false;
    }

    public void onKeyTyped(Character key) {
        if (!CharacterHelper.isPrintable(key)) {
            return;
        }

        var cursor = this.component.getMouseCursor();

        TextElement element = TextElementFactory.fromChar(//
                this.storage, //
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
            this.component.getMouseCursor().removeSelection();
            keyPressedCursorMove(keyCode);
            break;
        case DELETE:
            keyPressedDelete();
            break;
        case BACK_SPACE:
            keyPressedBackSpace();
            break;
        case CTRL:
            ctrlPressed = true;
            break;
        case F1:
            undo();
            break;
        case F2:
            redo();
            break;
        case F3:
            copy();
            break;
        case F4:
            paste();
            break;
        default:
            break;
        }
    }

    public void undo() {
        var cursorPosition = this.commandDeque.undo();

        var cursor = this.component.getMouseCursor();
        cursor.moveCursorTo(cursorPosition);

        this.component.getTextRepresentation().redraw();
    }

    public void redo() {
        var cursorPosition = this.commandDeque.redo();

        var cursor = this.component.getMouseCursor();
        cursor.moveCursorTo(cursorPosition);

        this.component.getTextRepresentation().redraw();
    }

    public void copy() {
        LOGGER.trace("COPY");
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        var cursor = this.component.getMouseCursor();
        StringBuilder textSelected = new StringBuilder(cursor.getSelectedText().size());
        for (var element : cursor.getSelectedText()) {
            textSelected.append(element.toString());
        }

        StringSelection strSel = new StringSelection(textSelected.toString());

        clipboard.setContents(strSel, null);
    }

    public void paste() {
        LOGGER.trace("PASTE");

        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        String textFromClipboard;

        try {

            //TODO
            clipboardConverter.read();

            textFromClipboard = clipboard.getData(DataFlavor.stringFlavor).toString();
        } catch (UnsupportedFlavorException | IOException e) {
            LOGGER.error("Can't read data from clipboard. ", e);
            return;
        }

        var cursor = this.component.getMouseCursor();
        cursor.removeSelection();

        var command = new TextElementAddRowTextBeforeCommand(storage, textFromClipboard, cursor.getPosition());

        doTextCommand(command);
    }

    private void doTextCommand(TextCommand command) {
        command.execute();

        if (command.isEmpty()) {
            return;
        }

        this.component.getTextRepresentation().redraw();

        this.commandDeque.addCommand(command);
    }

    public void onKeyReleased(KeyCode keyCode) {
        switch (keyCode) {
        case CTRL:
            ctrlPressed = false;
            break;
        default:
            break;
        }
    }

    private void keyPressedEnter() {
        var cursor = this.component.getMouseCursor();

        TextElement addedElement;

        if (ctrlPressed) {
            addedElement = TextElementFactory.genNewSectionChar(//
                    this.storage, //
                    cursor.getTextFormat(), //
                    cursor.getTextFormatEffect()//
            );
        } else {
            addedElement = TextElementFactory.genNewLineChar(//
                    this.storage, //
                    cursor.getTextFormat(), //
                    cursor.getTextFormatEffect()//
            );
        }
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

        doTextCommand(addCommand);

        cursor.moveCursorTo(addCommand.getCursorPosition());
    }

    private void keyPressedBackSpace() {
        var cursor = this.component.getMouseCursor();

        TextCommand deleteCommand;
        if (cursor.isTextSelected()) {
            deleteCommand = new TextElementRemoveSelectedCommand(cursor.getSelectedText());
            cursor.removeSelection();
        } else {
            deleteCommand = new TextRemoveBevorCommand(cursor.getPosition());
        }

        doTextCommand(deleteCommand);

        cursor.moveCursorTo(deleteCommand.getCursorPosition());
    }

    private void keyPressedDelete() {
        var cursor = this.component.getMouseCursor();

        TextCommand deleteCommand;
        if (cursor.isTextSelected()) {
            deleteCommand = new TextElementRemoveSelectedCommand(cursor.getSelectedText());
            cursor.removeSelection();
        } else {
            deleteCommand = new TextElementRemoveCommand(cursor.getPosition());
        }

        doTextCommand(deleteCommand);

        cursor.moveCursorTo(deleteCommand.getCursorPosition());

    }

    private boolean keyPressedCursorMove(KeyCode keyCode) {
        var cursor = this.component.getMouseCursor();
        var representation = this.component.getTextRepresentation();

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
