package net.eugenpaul.jlexi.component.text.format.structure;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Setter
public class TextFooterConfiguration {
    @Default
    private boolean onFirstPage = true;

    public TextFooterConfiguration copy() {
        return new TextFooterConfiguration(//
                onFirstPage //
        );
    }
}
