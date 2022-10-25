package net.eugenpaul.jlexi.component.text.converter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.structure.TextFooterData;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderData;
import net.eugenpaul.jlexi.component.text.format.structure.TextSection;

@AllArgsConstructor
@Getter
public class TextData {
    private TextHeaderData header;
    private List<TextSection> sections;
    private TextFooterData footer;
}
