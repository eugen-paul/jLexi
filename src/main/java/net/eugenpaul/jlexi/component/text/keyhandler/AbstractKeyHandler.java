package net.eugenpaul.jlexi.component.text.keyhandler;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.command.TextCommand;
import net.eugenpaul.jlexi.command.TextElementAddBeforeCommand;
import net.eugenpaul.jlexi.command.TextElementAddFormatTextBeforeCommand;
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
import net.eugenpaul.jlexi.exception.UnsupportedException;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.event.KeyCode;
import net.eugenpaul.jlexi.utils.helper.CharacterHelper;

@Slf4j
public class AbstractKeyHandler {

    private final KeyHandlerable component;
    private final ResourceManager storage;
    private final CommandsDeque<TextPosition, TextCommand> commandDeque;
    private final ClipboardConverter clipboardConverter;

    protected AbstractKeyHandler(KeyHandlerable component, ResourceManager storage,
            CommandsDeque<TextPosition, TextCommand> commandDeque) {
        this.component = component;
        this.storage = storage;
        this.commandDeque = commandDeque;
        this.clipboardConverter = new ClipboardConverterImpl(storage);
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
        default:
            break;
        }
    }

    public void addSpecialCharacter(SpecialCharacter sc) {
        var cursor = this.component.getMouseCursor();

        switch (sc) {
        case SIDE_BREAK:
            TextElement sideBreakElement = TextElementFactory.genNewSectionChar(//
                    this.storage, //
                    cursor.getTextFormat(), //
                    cursor.getTextFormatEffect()//
            );
            addTextElement(cursor, sideBreakElement);
            break;
        case NEW_LINE:
            TextElement newLineElement = TextElementFactory.genNewLineChar(//
                    this.storage, //
                    cursor.getTextFormat(), //
                    cursor.getTextFormatEffect()//
            );
            addTextElement(cursor, newLineElement);
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
        var cursor = this.component.getMouseCursor();
        clipboardConverter.write(cursor.getSelectedText());
    }

    public void paste() {
        LOGGER.trace("PASTE");

        var cursor = this.component.getMouseCursor();
        List<TextElement> textFromClipboard;

        try {
            textFromClipboard = clipboardConverter.read(cursor.getTextFormat(), cursor.getTextFormatEffect());
        } catch (UnsupportedException e) {
            LOGGER.error("Can't read data from clipboard. ", e);
            return;
        }

        cursor.removeSelection();

        var command = new TextElementAddFormatTextBeforeCommand(textFromClipboard, cursor.getPosition());

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
        default:
            break;
        }
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

        cursor.moveCursorTo(addCommand.getData());
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

        cursor.moveCursorTo(deleteCommand.getData());
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

        cursor.moveCursorTo(deleteCommand.getData());

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
