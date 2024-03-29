package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.component.text.converter.ClipboardConverter;
import net.eugenpaul.jlexi.component.text.converter.clipboard.html.HtmlClipboardFormatHelper;
import net.eugenpaul.jlexi.component.text.converter.clipboard.html.HtmlToText;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.exception.UnsupportedException;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

@Slf4j
public class ClipboardConverterImpl implements ClipboardConverter {

    private static final String READ_ERROR = "Can't read data from clipboard. ";

    private final ResourceManager storage;
    private final HtmlToText htmlToTextConv;
    private final TextToClipboard textToClipboardConv;

    public ClipboardConverterImpl(ResourceManager storage) {
        this.storage = storage;
        this.htmlToTextConv = new HtmlToText(storage);
        this.textToClipboardConv = new TextToClipboard();
    }

    @Override
    public List<TextElement> read(TextFormat format, TextFormatEffect effect) throws NotYetImplementedException {
        String textFromClipboard;
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            if (clipboard.isDataFlavorAvailable(DataFlavor.allHtmlFlavor)) {
                textFromClipboard = clipboard.getData(DataFlavor.allHtmlFlavor).toString();
                return clipboardHtmlToText(textFromClipboard);
            } else if (clipboard.isDataFlavorAvailable(DataFlavor.selectionHtmlFlavor)) {
                textFromClipboard = clipboard.getData(DataFlavor.selectionHtmlFlavor).toString();
                return clipboardHtmlToText(textFromClipboard);
            } else if (clipboard.isDataFlavorAvailable(DataFlavor.fragmentHtmlFlavor)) {
                textFromClipboard = clipboard.getData(DataFlavor.fragmentHtmlFlavor).toString();
                return clipboardHtmlToText(textFromClipboard);
            }
            textFromClipboard = clipboard.getData(DataFlavor.stringFlavor).toString();
            return plainToText(textFromClipboard, storage, format, effect);
        } catch (UnsupportedFlavorException | IOException e) {
            LOGGER.error(READ_ERROR, e);
            throw new UnsupportedException(READ_ERROR, e);
        }
    }

    @Override
    public List<TextElement> readHtml(TextFormat format, TextFormatEffect effect)
            throws NotYetImplementedException, UnsupportedException {
        String textFromClipboard;
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            textFromClipboard = clipboard.getData(DataFlavor.allHtmlFlavor).toString();
        } catch (UnsupportedFlavorException | IOException e) {
            LOGGER.error(READ_ERROR, e);
            throw new UnsupportedException(READ_ERROR, e);
        }

        return clipboardHtmlToText(textFromClipboard);
    }

    @Override
    public List<TextElement> readPlain(TextFormat format, TextFormatEffect effect)
            throws NotYetImplementedException, UnsupportedException {
        String textFromClipboard;
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            textFromClipboard = clipboard.getData(DataFlavor.stringFlavor).toString();
        } catch (UnsupportedFlavorException | IOException e) {
            LOGGER.error(READ_ERROR, e);
            throw new UnsupportedException(READ_ERROR, e);
        }

        return plainToText(textFromClipboard, storage, format, effect);
    }

    @Override
    public void write(List<TextElement> text) throws NotYetImplementedException {
        var clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferData = textToClipboardConv.generateSelection(text);
        clipboard.setContents(transferData, null);
    }

    private List<TextElement> clipboardHtmlToText(String clipboardHtml) {
        var html = HtmlClipboardFormatHelper.extractHtml(clipboardHtml);
        return htmlToTextConv.convert(html);
    }

    private static List<TextElement> plainToText(String html, ResourceManager storage, TextFormat format,
            TextFormatEffect effect) {

        List<TextElement> responseText = new LinkedList<>();

        for (var c : html.toCharArray()) {
            var element = TextElementFactory.fromChar(//
                    storage, //
                    c, //
                    format, //
                    effect);

            responseText.add(element);
        }

        return responseText;
    }

    // TODO: Add copy/paste RTF formatted text
    @SuppressWarnings("unused")
    private static String rtfToHtml(Reader rtf) throws IOException {
        JEditorPane p = new JEditorPane();
        p.setContentType("text/rtf");
        EditorKit kitRtf = p.getEditorKitForContentType("text/rtf");
        try {
            kitRtf.read(rtf, p.getDocument(), 0);
            EditorKit kitHtml = p.getEditorKitForContentType("text/html");
            Writer writer = new StringWriter();
            kitHtml.write(writer, p.getDocument(), 0, p.getDocument().getLength());
            return writer.toString();
        } catch (BadLocationException e) {
            LOGGER.error("Error by convertRTF to HTML", e);
        }
        return null;
    }
}
