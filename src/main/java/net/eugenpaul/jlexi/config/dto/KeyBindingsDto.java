package net.eugenpaul.jlexi.config.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public final class KeyBindingsDto {
    private String name;
    private List<KeyBindingsParameterDto> parameter;
}
