package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Iterator;

import net.eugenpaul.jlexi.component.text.format.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextSection extends TextStructure implements GlyphIterable<TextStructureForm> {

    public TextSection(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        super(parentStructure, format, storage);
    }

    @Override
    public void resetStructure() {
        structureForm = null;
        children.stream().forEach(TextStructure::resetStructure);
    }

    @Override
    protected void restructChildren() {
        // TODO
    }

    @Override
    public Iterator<TextStructureForm> printableChildIterator() {
        // TODO
        return null;
    }

    @Override
    public boolean childCompleteTest() {
        // TODO Auto-generated method stub
        return true;
    }

}
