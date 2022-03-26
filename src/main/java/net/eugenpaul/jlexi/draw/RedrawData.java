package net.eugenpaul.jlexi.draw;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eugenpaul.jlexi.utils.Area;

@AllArgsConstructor
@Getter
public class RedrawData {
    String source;
    Drawable drawable;
    Area area;
}
