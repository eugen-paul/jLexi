package net.eugenpaul.jlexi.component.text.converter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.structure.TextFooterDataV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderDataV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextSectionV2;

@AllArgsConstructor
@Getter
public class TextDataV2 {
    private TextHeaderDataV2 header;
    private List<TextSectionV2> sections;
    private TextFooterDataV2 footer;
}
