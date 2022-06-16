package net.eugenpaul.jlexi.component.text.format.compositor.wordtorow;

import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.element.TextChar;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextWord;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneElementRow;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Builder
public class TextWordsToRowCompositorImpl implements TextWordsToRowCompositor {

    @Override
    public TextWordsToRowCompositor copy() {
        return new TextWordsToRowCompositorImpl();
    }

    @Override
    public List<TextRepresentation> compose(List<TextWord> words, Size maxSize) {
        List<TextRepresentation> responseRows = new LinkedList<>();
        LinkedList<TextElement> elementsToRow = new LinkedList<>();

        int currentLength = 0;
        int currentHeight = 0;
        for (TextWord word : words) {
            boolean lastSylableWithWordBreak = false;
            for (var syllable : word.getSyllables()) {
                int syllableLength = syllable.getLength();
                boolean currentSylableWithWordBreak = syllable.isWithWordBreak();

                if (currentLength + syllableLength <= maxSize.getWidth() || elementsToRow.isEmpty()) {
                    if (lastSylableWithWordBreak && !elementsToRow.isEmpty()) {
                        TextElement lastwb = elementsToRow.removeLast();
                        currentLength -= lastwb.getSize().getWidth();
                    }
                    elementsToRow.addAll(syllable.getElements());
                    currentLength += syllableLength;
                    currentHeight = Math.max(currentHeight, syllable.getHeight());
                } else {
                    setRelativPositions(elementsToRow, currentHeight);
                    TextPaneElementRow row = createRow(elementsToRow);
                    responseRows.add(row);

                    elementsToRow.clear();
                    elementsToRow.addAll(syllable.getElements());
                    currentLength = syllableLength;
                    currentHeight = syllable.getHeight();
                }

                lastSylableWithWordBreak = currentSylableWithWordBreak;
            }
        }

        if (!elementsToRow.isEmpty()) {
            setRelativPositions(elementsToRow, currentHeight);
            TextPaneElementRow row = createRow(elementsToRow);
            responseRows.add(row);
        }

        return responseRows;
    }

    private TextPaneElementRow createRow(List<TextElement> elementsToRow) {
        return new TextPaneElementRow(null, elementsToRow);
    }

    private void setRelativPositions(List<TextElement> elementsToRow, int maxHeight) {
        int currentX = 0;
        for (TextElement element : elementsToRow) {
            if (element instanceof TextChar) {
                var charElement = (TextChar) element;
                int d = charElement.getDescent();
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
