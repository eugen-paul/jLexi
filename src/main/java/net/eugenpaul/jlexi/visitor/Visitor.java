package net.eugenpaul.jlexi.visitor;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.text.structure.CharGlyph;

public interface Visitor {

    public void visit(CharGlyph glaph);

    public void visit(Glyph glaph);
}
