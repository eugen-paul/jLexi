package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.Iterator;

import net.eugenpaul.jlexi.component.interfaces.GlyphIterable;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.representation.TextStructureForm;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class TextSection extends TextStructureOfStructure implements GlyphIterable<TextStructureForm> {

    public TextSection(TextStructure parentStructure, TextFormat format, ResourceManager storage) {
        super(parentStructure, format, storage);
    }

    @Override
    public Iterator<TextStructureForm> drawableChildIterator() {
        // TODO
        return null;
    }

    @Override
    protected boolean checkMergeWith(TextStructure element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    protected TextElement mergeWithNext(TextStructure element) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected TextElement mergeWithPrevious(TextStructure element) {
        // TODO Auto-generated method stub
        return null;
    }

}
