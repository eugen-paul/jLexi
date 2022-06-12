package net.eugenpaul.jlexi.component.text.format.compositor.wordtorow;

import java.util.LinkedList;
import java.util.List;

import lombok.Builder;
import net.eugenpaul.jlexi.component.text.format.element.TextChar;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.component.text.format.element.TextWord;
import net.eugenpaul.jlexi.component.text.format.element.TextWordBreak;
import net.eugenpaul.jlexi.component.text.format.representation.TextPaneElementRow;
import net.eugenpaul.jlexi.component.text.format.representation.TextRepresentation;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;
import net.eugenpaul.jlexi.utils.Size;
import net.eugenpaul.jlexi.utils.Vector2d;

@Builder
public class TextWordsToRowCompositorImpl implements TextWordsToRowCompositor {

    @Override
    public TextWordsToRowCompositor copy() {
        return new TextWordsToRowCompositorImpl();
    }

    @Override
    public List<TextRepresentation> compose(List<TextWord> words, Size maxSize, ResourceManager storage) {
        List<TextRepresentation> responseRows = new LinkedList<>();
        List<TextElement> elementsToRow = new LinkedList<>();

        int currentLength = 0;
        int currentHeight = 0;
        for (TextWord word : words) {
            boolean firstSyllable = true;

            for (var syllable : word.getSyllables()) {
                int syllableWidth = getSyllableWidth(syllable);
                if (currentLength + syllableWidth <= maxSize.getWidth() || elementsToRow.isEmpty()) {
                    elementsToRow.addAll(syllable);
                    currentLength += syllableWidth;
                    currentHeight = Math.max(currentHeight, getSyllableHeight(syllable));
                } else {
                    if (!firstSyllable) {
                        // add word break
                        TextWordBreak wb = new TextWordBreak(//
                                null, //
                                storage, //
                                null, //
                                TextFormat.DEFAULT, //
                                TextFormatEffect.DEFAULT_FORMAT_EFFECT, //
                                syllable.get(0) //
                        );
                        elementsToRow.add(wb);
                        currentHeight = Math.max(currentHeight, getSyllableHeight(syllable));
                    }

                    setRelativPositions(elementsToRow, currentHeight);
                    TextPaneElementRow row = createRow(elementsToRow);
                    responseRows.add(row);

                    elementsToRow.clear();
                    elementsToRow.addAll(syllable);
                    currentLength = syllableWidth;
                    currentHeight = getSyllableHeight(syllable);
                }

                firstSyllable = false;
            }
        }

        if (!elementsToRow.isEmpty()) {
            setRelativPositions(elementsToRow, currentHeight);
            TextPaneElementRow row = createRow(elementsToRow);
            responseRows.add(row);
        }

        return responseRows;
    }

    private int getSyllableWidth(List<TextElement> syllable) {
        return syllable.stream()//
                .mapToInt(v -> v.getSize().getWidth())//
                .sum();
    }

    private int getSyllableHeight(List<TextElement> syllable) {
        int maxHeight = 0;
        for (TextElement textElement : syllable) {
            maxHeight = Math.max(maxHeight, textElement.getSize().getHeight());
        }
        return maxHeight;
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
