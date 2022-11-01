package net.eugenpaul.jlexi.component.text.converter.json.format;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderConfiguration;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JsonHeader {
    private boolean onFirstPage = true;

    private List<JsonParagraph> paragraphs = Collections.emptyList();

    public TextHeaderConfiguration getConfiguration() {
        return TextHeaderConfiguration.builder()//
                .onFirstPage(onFirstPage)//
                .build();
    }
}
