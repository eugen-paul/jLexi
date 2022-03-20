package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;

import net.eugenpaul.jlexi.component.text.format.TextDocumentElement;

public interface Splitable<T extends TextDocumentElement> {
    public List<T> getSplits();

    public void clearSplitter();
}
