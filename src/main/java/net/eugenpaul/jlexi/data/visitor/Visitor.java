package net.eugenpaul.jlexi.data.visitor;

import net.eugenpaul.jlexi.data.stucture.CharGlyph;
import net.eugenpaul.jlexi.data.stucture.Row;

public interface Visitor {

    public void visit(CharGlyph glaph);

    public void visit(Row glaph);
}
