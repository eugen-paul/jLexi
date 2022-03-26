package net.eugenpaul.jlexi.window.propertychanges;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateTitle {
    private String target;
    private String newTitle;
}