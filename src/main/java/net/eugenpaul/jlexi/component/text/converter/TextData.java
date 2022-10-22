package net.eugenpaul.jlexi.component.text.converter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.eugenpaul.jlexi.component.text.format.structure.TextFooter;
import net.eugenpaul.jlexi.component.text.format.structure.TextFooterTemplate;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeader;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderTemplate;
import net.eugenpaul.jlexi.component.text.format.structure.TextSection;

@AllArgsConstructor
@Getter
public class TextData {
    private TextHeaderTemplate header;
    private List<TextSection> sections;
    private TextFooterTemplate footer;
}
