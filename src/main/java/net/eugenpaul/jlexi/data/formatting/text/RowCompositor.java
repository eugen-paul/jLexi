package net.eugenpaul.jlexi.data.formatting.text;

import java.util.List;

import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.data.formatting.Composition;
import net.eugenpaul.jlexi.data.formatting.Compositor;

public class RowCompositor implements Compositor<Glyph> {

    private Composition<Glyph> composition;

    public RowCompositor() {
        composition = null;
    }

    @Override
    public void setComposition(Composition<Glyph> composition) {
        this.composition = composition;
    }

    @Override
    public Glyph compose(List<Glyph> data) {
        return null;
    }

}
