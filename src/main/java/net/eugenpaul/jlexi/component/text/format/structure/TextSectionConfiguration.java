package net.eugenpaul.jlexi.component.text.format.structure;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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

    public TextSectionConfiguration copy() {
        return new TextSectionConfiguration(//
                numberOfColumns, //
                siteWidthPx, //
                siteHeightPx //
        );
    }
}
