package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import net.eugenpaul.jlexi.component.text.converter.clipboard.html.ToHtmlConvertHelper;
import net.eugenpaul.jlexi.component.text.converter.clipboard.html.ToHtmlConvertHelperImpl;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class ToClipboardSelection implements Transferable {

    private static List<DataFlavor> textFlavors = List.of(//
            DataFlavor.stringFlavor, //
            DataFlavor.fragmentHtmlFlavor //
    );

    private String plainText;
    private String html;

    public ToClipboardSelection(List<TextElement> text) {
        this(//
                text, //
                new ToPlainConvertHelperImpl(), //
                new ToHtmlConvertHelperImpl() //
        );
    }

    public ToClipboardSelection(List<TextElement> text, ToPlainConvertHelper plainConv, ToHtmlConvertHelper htmlConv) {
        this.plainText = plainConv.toPlain(text);
        this.html = htmlConv.toHtml(text);
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return textFlavors.toArray(new DataFlavor[textFlavors.size()]);
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return textFlavors.contains(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        String toBeExported = null;
        if (flavor == DataFlavor.stringFlavor) {
            toBeExported = plainText;
        } else if (flavor == DataFlavor.fragmentHtmlFlavor) {
            toBeExported = html;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }

        if (String.class.equals(flavor.getRepresentationClass())) {
            return toBeExported;
        } else if (Reader.class.equals(flavor.getRepresentationClass())) {
            return new StringReader(toBeExported);
        }

        throw new UnsupportedFlavorException(flavor);
    }

}
