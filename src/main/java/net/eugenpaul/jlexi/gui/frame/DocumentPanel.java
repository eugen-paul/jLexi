package net.eugenpaul.jlexi.gui.frame;

import java.beans.PropertyChangeEvent;
import java.awt.Dimension;
import java.awt.image.MemoryImageSource;

import lombok.Getter;
import net.eugenpaul.jlexi.controller.DefaultController;
import net.eugenpaul.jlexi.controller.ViewPropertyChangeType;
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
        this.panel.setDoubleBuffered(true);
        this.panel.addComponentListener(new ResizeListner(controller));
        this.panel.addMouseListener(new MouseListner(controller));
        this.panel.addKeyListener(new KeyListener(controller));
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
        if (evt.getPropertyName().equalsIgnoreCase(ViewPropertyChangeType.UPDATE.toString())) {
            draw();
        }
    }

    private void draw() {
        Drawable drawable = glyph.getPixels();
        panel.update((new MemoryImageSource(//
                drawable.getPixelSize().getWidth(), //
                drawable.getPixelSize().getHight(), //
                drawable.getPixels(), //
                0, //
                drawable.getPixelSize().getWidth()//
        )//
        ));
    }

}
