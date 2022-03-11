package net.eugenpaul.jlexi.component.text.converter.html;

import java.io.File;

import net.eugenpaul.jlexi.component.text.converter.TextConverter;
// import net.eugenpaul.jlexi.component.text.TextPaneElement;
// import net.eugenpaul.jlexi.component.text.converter.TextConverter;
// import net.eugenpaul.jlexi.component.text.elements.CharGlyph;
// import net.eugenpaul.jlexi.component.text.format.container.Bold;
// import net.eugenpaul.jlexi.component.text.format.container.Italic;
// import net.eugenpaul.jlexi.component.text.format.container.Paragraph;
// import net.eugenpaul.jlexi.component.text.format.container.Text;
// import net.eugenpaul.jlexi.component.text.format.container.TextFormatContainer;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.container.NodeList;

public class HtmlConverter implements TextConverter {

    private FontStorage fontStorage;

    public HtmlConverter(FontStorage fontStorage) {
        this.fontStorage = fontStorage;
    }

    // @Override
    // public NodeList<TextPaneElement> read(File file) {

    //     NodeList<TextPaneElement> responseNodeList = new NodeList<>();

        // TextFormatContainer<TextPaneElement> paragraph = new Paragraph<>(null, null);
        // var el = responseNodeList.addFirst(paragraph);
        // paragraph.setTextPaneListElement(el);

        // Text<TextPaneElement> a = new Text<>(paragraph, paragraph);
        // paragraph.add(a);

        // String aString = "Strign a.";
        // for (int i = 0; i < aString.length(); i++) {
        //     CharGlyph glyph = new CharGlyph(a, aString.charAt(i), fontStorage, null);
        //     a.add(glyph);
        // }

        // Bold<TextPaneElement> b = new Bold<>(paragraph, paragraph);
        // paragraph.add(b);

        // String bString = "Bold String";
        // for (int i = 0; i < bString.length(); i++) {
        //     CharGlyph glyph = new CharGlyph(b, bString.charAt(i), fontStorage, null);
        //     b.add(glyph);
        // }

        // Italic<TextPaneElement> c = new Italic<>(paragraph, paragraph);
        // paragraph.add(c);

        // String cString = "Italic String";
        // for (int i = 0; i < cString.length(); i++) {
        //     CharGlyph glyph = new CharGlyph(c, cString.charAt(i), fontStorage, null);
        //     c.add(glyph);
        // }

    //     return responseNodeList;
    // }

    // @Override
    // public File write(NodeList<TextPaneElement> rawList) {
    //     // TODO Auto-generated method stub
    //     return null;
    // }

}
