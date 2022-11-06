package net.eugenpaul.jlexi.component.text.format.compositor.wordtorow;

import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneElement;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneRowV2;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentationV2;
import net.eugenpaul.jlexi.component.text.format.structure.textelements.TextWordV2;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Builder
public class TextWordsToRowCompositorImplV2 implements TextWordsToRowCompositorV2 {

    @Override
    public TextWordsToRowCompositorV2 copy() {
        return new TextWordsToRowCompositorImplV2();
    }

    @Override
    public List<TextRepresentationV2> compose(List<TextWordV2> words, Size maxSize) {
        List<TextRepresentationV2> responseRows = new LinkedList<>();
        LinkedList<TextRepresentationV2> rowData = new LinkedList<>();

        int currentLength = 0;
        int currentHeight = 0;
        // TODO : do it with listiterator
        for (TextWordV2 word : words) {
            boolean lastSylableWithWordBreak = false;
            for (var syllable : word.getSyllables()) {
                int syllableLength = syllable.getLength();
                boolean currentSylableWithWordBreak = syllable.isWithWordBreak();

                if (currentLength + syllableLength <= maxSize.getWidth() || rowData.isEmpty()) {
                    if (lastSylableWithWordBreak && !rowData.isEmpty()) {
                        TextRepresentationV2 lastwb = rowData.removeLast();
                        currentLength -= lastwb.getSize().getWidth();
                    }
                    syllable.getElements().stream()//
                            .map(v -> v.getRepresentation(maxSize))//
                            .forEach(rowData::addAll);
                    currentLength += syllableLength;
                    currentHeight = Math.max(currentHeight, syllable.getHeight());
                } else {
                    setRelativPositions(rowData, currentHeight);
                    TextPaneRowV2 row = createRow(rowData);
                    responseRows.add(row);

                    rowData.clear();
                    syllable.getElements().stream()//
                            .map(v -> v.getRepresentation(maxSize))//
                            .forEach(rowData::addAll);
                    currentLength = syllableLength;
                    currentHeight = syllable.getHeight();
                }

                lastSylableWithWordBreak = currentSylableWithWordBreak;
            }
        }

        if (!rowData.isEmpty()) {
            setRelativPositions(rowData, currentHeight);
            TextPaneRowV2 row = createRow(rowData);
            responseRows.add(row);
        }

        return responseRows;
    }

    private TextPaneRowV2 createRow(List<TextRepresentationV2> rowData) {
        var row = new TextPaneRowV2(null);
        rowData.forEach(row::add);
        return row;
    }

    private void setRelativPositions(List<TextRepresentationV2> elementsToRow, int maxHeight) {
        int currentX = 0;
        for (var element : elementsToRow) {
            if (element instanceof TextPaneElement) {
                var charElement = (TextPaneElement) element;
                int d = charElement.getSize().getHeight();
                element.setRelativPosition(new Vector2d(//
                        currentX, //
                        Math.max(0, maxHeight - element.getSize().getHeight() - d)//
                ));
            } else {
                element.setRelativPosition(new Vector2d(//
                        currentX, //
                        maxHeight - element.getSize().getHeight() //
                ));
            }
            currentX += element.getSize().getWidth();
        }
    }

}
