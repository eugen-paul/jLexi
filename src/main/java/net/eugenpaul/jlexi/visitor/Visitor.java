package net.eugenpaul.jlexi.visitor;

import net.eugenpaul.jlexi.component.text.structure.CharGlyph;
import net.eugenpaul.jlexi.component.text.structure.TextRow;

public interface Visitor {

    public void visit(CharGlyph glaph);

    public void visit(TextRow glaph);
}
