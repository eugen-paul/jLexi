package net.eugenpaul.jlexi.gui.frame;

import java.beans.PropertyChangeEvent;
import java.awt.Dimension;
import java.awt.image.MemoryImageSource;

import lombok.Getter;
import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.data.Drawable;
import net.eugenpaul.jlexi.data.Glyph;
import net.eugenpaul.jlexi.gui.AbstractPanel;

public class DocumentPanel extends AbstractPanel {
    private Glyph glyph;
    @Getter
    private ImagePanel panel;

    public DocumentPanel(Glyph glyph, DefaultController controller) {
        super(controller);
        this.glyph = glyph;
        this.panel = new ImagePanel();
        this.panel.addComponentListener(new ResizeListner(controller));
        this.panel.setPreferredSize(new Dimension(glyph.getSize().getWidth(), glyph.getSize().getHight()));
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public void setVisible(boolean status) {
        // Nothing to do
    }

    @Override
    public void modelPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equalsIgnoreCase("UPDATE")) {
            draw();
        }
    }

    private void draw() {
        Drawable drawable = glyph.getPixels();

        int h = drawable.getPixels().length;
        int w = drawable.getPixels()[0].length;

        int[] pixels = new int[w * h];

        for (int lineNumber = 0; lineNumber < h; lineNumber++) {
            System.arraycopy(drawable.getPixels()[lineNumber], 0, pixels, lineNumber * w, w);
        }

        panel.update((new MemoryImageSource(w, h, pixels, 0, w)));
    }

}
