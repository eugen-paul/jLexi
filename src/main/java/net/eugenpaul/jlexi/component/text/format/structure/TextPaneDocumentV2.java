package net.eugenpaul.jlexi.component.text.format.structure;

import java.util.List;

import lombok.var;
import net.eugenpaul.jlexi.component.interfaces.ChangeListener;
import net.eugenpaul.jlexi.component.text.converter.TextDataV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextNewLineV2;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;

public class TextPaneDocumentV2 extends TextStructureOfStructureV2 {

    private ChangeListener parent;
    private ResourceManager storage;
    private TextHeaderCreaterV2 headerCreater;
    private TextFooterCreaterV2 footerCreater;

    public TextPaneDocumentV2(ResourceManager storage, ChangeListener parent) {
        super(null);
        this.parent = parent;
        this.storage = storage;
        this.headerCreater = null;
        initEmptyDocument();
    }

    public void setText(TextDataV2 data) {
        this.children.clear();

        if (data.getHeader() != null) {
            this.headerCreater = new TextHeaderCreaterV2(//
                    data.getHeader().getHeaderText(), //
                    data.getHeader().getConfiguration() //
            );
        } else {
            this.headerCreater = null;
        }

        if (data.getFooter() != null) {
            this.footerCreater = new TextFooterCreaterV2( //
                    data.getFooter().getFooterText(), //
                    data.getFooter().getConfiguration() //
            );
        } else {
            this.footerCreater = null;

        }

        for (var child : data.getSections()) {
            this.children.add(child);
            child.setParentStructure(this);
            child.setHeaderCreater(this.headerCreater);
            child.setFooterCreater(this.footerCreater);
        }

        setRepresentation(null);
    }

    @Override
    public List<TextRepresentationV2> getRepresentation(Size size) {
        if (this.headerCreater != null) {
            this.headerCreater.reset();
        }
        if (this.footerCreater != null) {
            this.footerCreater.reset();
        }
        setRepresentation(null);
        return super.getRepresentation(size);
    }

    private void initEmptyDocument() {
        TextSectionV2 section = new TextSectionV2(this, TextSectionConfiguration.builder().build());
        TextParagraphV2 paragraph = new TextParagraphV2(section, this.storage);

        paragraph.add(new TextNewLineV2(storage, paragraph));

        this.children.add(section);
    }

    @Override
    public void notifyChangeUp() {
        setRepresentation(null);
        if (this.parent != null) {
            this.parent.notifyChange();
        }
    }

    @Override
    protected boolean checkMergeWith(TextStructureV2 element) {
        return false;
    }

    @Override
    protected boolean canContainChild(TextStructureV2 element) {
        return element instanceof TextSectionV2;
    }

    @Override
    protected TextRemoveResponseV2 mergeWith(TextStructureV2 element) {
        // Document ist the root class and cann't be merged with other elements
        return TextRemoveResponseV2.EMPTY;
    }

    @Override
    protected TextStructureOfStructureV2 copyStructure() {
        var doc = new TextPaneDocumentV2(storage, parent);
        doc.children.clear();
        return doc;
    }

    @Override
    protected boolean hasToBeSplited(TextStructureV2 newChild) {
        return false;
    }

    @Override
    protected boolean isComplete() {
        return true;
    }

    @Override
    protected void setComplete() {
        // nothing todo
    }

    @Override
    protected TextMergeResponseV2 doMerge(TextStructureV2 next) {
        return TextMergeResponseV2.EMPTY;
    }

    @Override
    protected TextSplitResponse doSplit(TextStructureV2 position) {
        return TextSplitResponse.EMPTY;
    }
}
