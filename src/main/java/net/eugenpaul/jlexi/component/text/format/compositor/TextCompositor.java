package net.eugenpaul.jlexi.component.text.format.compositor;

import java.util.Iterator;
import java.util.List;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.structure.TextStructure;

public interface TextCompositor {
    public List<TextStructure> compose(Iterator<TextElement> iterator);
}
