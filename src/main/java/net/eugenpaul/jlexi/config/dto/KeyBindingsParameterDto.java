package net.eugenpaul.jlexi.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public final class KeyBindingsParameterDto {
    private String name;
    private String keys;
    private String description;
}
