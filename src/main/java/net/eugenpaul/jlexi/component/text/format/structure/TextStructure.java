package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.FormatAttribute;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.FontStorage;
import net.eugenpaul.jlexi.utils.Size;

public abstract class TextStructure {
    private FormatAttribute format;

    protected List<TextStructureForm> structureForm;
    protected FontStorage fontStorage;
    private TextStructure parentStructure;

    protected TextStructure(TextStructure parentStructure, FormatAttribute format, FontStorage fontStorage) {
        this.parentStructure = parentStructure;
        this.format = format;
        this.fontStorage = fontStorage;
        this.structureForm = null;
    }

    public FormatAttribute mergeFormat(FormatAttribute format) {
        format = format.merge(this.format);
        if (null != parentStructure) {
            format = parentStructure.mergeFormat(format);
        }
        return format;
    }

    public abstract void setFormat(TextElement from, TextElement to);

    public void notifyChange() {
        structureForm = null;
        if (null != parentStructure) {
            parentStructure.notifyChange();
        }
    }

    public abstract List<TextStructureForm> getRows(Size size);

    public abstract void resetStructure();
}
