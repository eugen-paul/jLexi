package net.eugenpaul.jlexi.gui.frame;

import java.awt.Graphics;

import javax.swing.JPanel;

import java.awt.Image;
import java.awt.image.MemoryImageSource;

public class ImagePanel extends JPanel {
    private Image img = null;

    @Override
    public void paint(Graphics g) {
        if (this.img != null) {
            g.drawImage(img, 0, 0, this);
        }
    }

    public void update(MemoryImageSource img) {
        this.img = createImage(img);
    }
}
