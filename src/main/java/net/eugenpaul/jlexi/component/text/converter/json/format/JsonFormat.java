package net.eugenpaul.jlexi.component.text.converter.json.format;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JsonFormat {

    private List<JsonElement> data;
}
