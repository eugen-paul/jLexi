package net.eugenpaul.jlexi.visitor;

import net.eugenpaul.jlexi.component.Glyph;

public interface Visitor {
    public void visit(Glyph glaph);
}
