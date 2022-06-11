package net.eugenpaul.jlexi.component.text.format.structure;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder.Default;

@Builder
@Getter
@Setter
public class TextSectionConfiguration {
    @Default
    private int numberOfColumns = 1;
    @Default
    private int siteWidthPx = 595;
    @Default
    private int siteHeightPx = 842;
}
