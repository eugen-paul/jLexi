package net.eugenpaul.jlexi.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public final class TextEditorKeysDto {
    private String copy;
    private String paste;
    private String undo;
    private String redo;
}
