package net.eugenpaul.jlexi.window.impl.swing.frame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.eugenpaul.jlexi.draw.Drawable;

public class ImagePanel extends JPanel {
    private transient Drawable currentDrawable;
    private transient Image currentImage;

    private final transient Object imgSynch = new Object();

    /**
     * C'tor
     */
    public ImagePanel() {
        currentDrawable = null;
        currentImage = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        synchronized (imgSynch) {
            if (currentDrawable != null) {
                g.clearRect(0, 0, getWidth(), getHeight());

                if (null == currentImage) {
                    currentImage = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(//
                            currentDrawable.getSize().getWidth(), //
                            currentDrawable.getSize().getHeight(), //
                            currentDrawable.asArgbPixels(), //
                            0, //
                            currentDrawable.getSize().getWidth()//
                    ));
                }

                g.drawImage(currentImage, 0, 0, null);
            }
        }
    }

    public void update(Drawable drawable) {
        SwingUtilities.invokeLater(() -> {
            synchronized (imgSynch) {
                currentDrawable = drawable;
                currentImage = null;
                repaint();
            }
        });
    }

}