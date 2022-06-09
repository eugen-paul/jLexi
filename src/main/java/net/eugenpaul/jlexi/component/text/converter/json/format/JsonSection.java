package net.eugenpaul.jlexi.component.text.converter.json.format;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JsonSection {
    private List<JsonParagraph> paragraphs = Collections.emptyList();
}
