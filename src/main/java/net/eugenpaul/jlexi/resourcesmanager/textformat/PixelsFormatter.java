package net.eugenpaul.jlexi.resourcesmanager.textformat;

import net.eugenpaul.jlexi.draw.Drawable;
import net.eugenpaul.jlexi.draw.DrawableV2Sketch;

public interface PixelsFormatter {
    public void doFormat(Drawable draw);
    public void doFormat(DrawableV2Sketch draw);
}
