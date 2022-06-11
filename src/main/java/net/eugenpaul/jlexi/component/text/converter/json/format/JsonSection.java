package net.eugenpaul.jlexi.component.text.converter.json.format;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.structure.TextSectionConfiguration;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JsonSection {
    private int numberOfColumns = 1;
    private int siteWidthPx = 595;
    private int siteHeightPx = 842;

    private List<JsonParagraph> paragraphs = Collections.emptyList();

    public TextSectionConfiguration getTextSectionConfiguration() {
        return TextSectionConfiguration.builder()//
                .numberOfColumns(numberOfColumns)//
                .siteWidthPx(siteWidthPx)//
                .siteHeightPx(siteHeightPx)//
                .build();
    }
}
