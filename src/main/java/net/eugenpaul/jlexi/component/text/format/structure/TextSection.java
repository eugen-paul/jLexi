package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;

public class TextSection extends TextStructure {

    protected TextSection(FormatAttribute format) {
        super(format);
        //TODO Auto-generated constructor stub
    }

    @Override
    public void setFormat(TextElement from, TextElement to) {

    }

    @Override
    public void notifyChange() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<TextStructure> getRows(int length) {
        // TODO Auto-generated method stub
        return null;
    }

}
