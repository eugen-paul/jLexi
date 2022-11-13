package net.eugenpaul.jlexi.component.text.converter.json.format;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.structure.TextFooterConfiguration;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JsonFooterV2 {
    private boolean onFirstPage = true;

    private List<JsonParagraphV2> paragraphs = Collections.emptyList();

    public TextFooterConfiguration getConfiguration() {
        return TextFooterConfiguration.builder()//
                .onFirstPage(onFirstPage)//
                .build();
    }
}
