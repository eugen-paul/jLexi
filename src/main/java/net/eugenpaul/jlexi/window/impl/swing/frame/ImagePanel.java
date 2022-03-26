package net.eugenpaul.jlexi.window.impl.swing.frame;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableImpl;
import net.eugenpaul.jlexi.utils.Area;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;
import net.eugenpaul.jlexi.utils.helper.ImageArrayHelper;

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
                            currentDrawable.getPixelSize().getWidth(), //
                            currentDrawable.getPixelSize().getHeight(), //
                            currentDrawable.getPixels(), //
                            0, //
                            currentDrawable.getPixelSize().getWidth()//
                    ));
                }

                g.drawImage(currentImage, 0, 0, null);
            }
        }
    }

    public void update(Drawable drawable) {
        SwingUtilities.invokeLater(() -> {
            synchronized (imgSynch) {
                if (null == currentDrawable //
                        || currentDrawable.getPixelSize().getHeight() != getHeight()//
                        || currentDrawable.getPixelSize().getWidth() != getWidth()//
                ) {
                    currentDrawable = createNewDrawable(drawable, new Area(Vector2d.zero(), drawable.getPixelSize()));
                } else {
                    ImageArrayHelper.copyRectangle(//
                            drawable, //
                            Vector2d.zero(), //
                            drawable.getPixelSize(), //
                            currentDrawable, //
                            Vector2d.zero() //
                    );
                }

                currentImage = null;
                repaint();
            }
        });
    }

    public void updateArea(Drawable drawable, Area area) {
        SwingUtilities.invokeLater(() -> {
            synchronized (imgSynch) {
                if (null == currentDrawable //
                        || currentDrawable.getPixelSize().getHeight() != getHeight()//
                        || currentDrawable.getPixelSize().getWidth() != getWidth()//
                ) {
                    currentDrawable = createNewDrawable(drawable, area);
                } else {
                    ImageArrayHelper.copyRectangle(//
                            drawable, //
                            Vector2d.zero(), //
                            area.getSize(), //
                            currentDrawable, //
                            area.getPosition() //
                    );
                }

                currentImage = null;
                repaint(//
                        area.getPosition().getX(), //
                        area.getPosition().getY(), //
                        area.getSize().getWidth(), //
                        area.getSize().getHeight() //
                );
            }
        });
    }

    private Drawable createNewDrawable(Drawable drawable, Area area) {
        int h = getHeight();
        int w = getWidth();
        int[] pixels = new int[w * h];
        Size pixelSize = new Size(w, h);
        Drawable response = new DrawableImpl(pixels, pixelSize);

        ImageArrayHelper.copyRectangle(//
                drawable, //
                Vector2d.zero(), //
                area.getSize(), //
                response, //
                area.getPosition() //
        );

        return response;
    }
}
