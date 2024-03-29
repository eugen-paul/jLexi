package net.eugenpaul.jlexi.component.text.converter.json;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.eugenpaul.jlexi.component.text.converter.TextConverter;
import net.eugenpaul.jlexi.component.text.converter.TextData;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonElement;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonFooter;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonFormat;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonHeader;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonParagraph;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonSection;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.component.text.format.structure.TextFooterData;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderData;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraph;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraphConfiguration;
import net.eugenpaul.jlexi.component.text.format.structure.TextSection;
import net.eugenpaul.jlexi.component.text.format.structure.TextSectionConfiguration;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class JsonConverter implements TextConverter {

    private final ObjectMapper mapper;

    private ResourceManager resourceManager;

    public JsonConverter(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.mapper = new ObjectMapper();
    }

    @Override
    public TextData read(String data) throws NotYetImplementedException {
        try {
            JsonFormat value = mapper.readValue(data, JsonFormat.class);

            var header = toHeader(value.getHeader());
            var footer = toFooter(value.getFooter());
            var sections = value.getSections().stream()//
                    .map(this::toSection)//
                    .collect(Collectors.toCollection(LinkedList::new));

            return new TextData(header, sections, footer);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error by parsing JSON-Data", e);
        }
    }

    private TextSection toSection(JsonSection input) {
        TextSection response = new TextSection(null, input.getTextSectionConfiguration());

        TextParagraph lastParagraph = null;

        for (var paragraph : input.getParagraphs()) {
            lastParagraph = toParagraph(paragraph);
            lastParagraph.setParentStructure(response);
            response.add(lastParagraph);
        }

        if (lastParagraph == null) {
            TextParagraph paragraph = new TextParagraph(//
                    null, //
                    TextParagraphConfiguration.builder().build(), //
                    resourceManager //
            );
            paragraph.setToEos(resourceManager);
            response.add(paragraph);
        } else {
            lastParagraph.setToEos(resourceManager);
        }

        return response;
    }

    private TextHeaderData toHeader(JsonHeader input) {
        var headerSection = new TextSection(null, TextSectionConfiguration.builder().block(true).build());
        var responseHeader = new TextHeaderData(headerSection, input.getConfiguration());

        TextParagraph lastParagraph = null;

        for (var paragraph : input.getParagraphs()) {
            lastParagraph = toParagraph(paragraph);
            lastParagraph.setParentStructure(headerSection);
            headerSection.add(lastParagraph);
        }

        if (lastParagraph == null) {
            TextParagraph paragraph = new TextParagraph(//
                    null, //
                    TextParagraphConfiguration.builder().build(), //
                    resourceManager //
            );
            paragraph.setToEos(resourceManager);
            headerSection.add(paragraph);
        } else {
            lastParagraph.setToEos(resourceManager);
        }

        return responseHeader;
    }

    private TextFooterData toFooter(JsonFooter input) {
        var headerSection = new TextSection(null, TextSectionConfiguration.builder().block(true).build());
        var responseHeader = new TextFooterData(headerSection, input.getConfiguration());

        TextParagraph lastParagraph = null;

        for (var paragraph : input.getParagraphs()) {
            lastParagraph = toParagraph(paragraph);
            lastParagraph.setParentStructure(headerSection);
            headerSection.add(lastParagraph);
        }

        if (lastParagraph == null) {
            TextParagraph paragraph = new TextParagraph(//
                    null, //
                    TextParagraphConfiguration.builder().build(), //
                    resourceManager //
            );
            paragraph.setToEos(resourceManager);
            headerSection.add(paragraph);
        } else {
            lastParagraph.setToEos(resourceManager);
        }

        return responseHeader;
    }

    private TextParagraph toParagraph(JsonParagraph input) {
        TextParagraph response = new TextParagraph(//
                null, //
                input.getTextSectionConfiguration(), //
                resourceManager //
        );

        for (var element : input.getElements()) {
            toElement(element).stream()//
                    .forEach(response::add);
        }

        response.setToEol(resourceManager);

        return response;
    }

    private List<TextElement> toElement(JsonElement input) {
        var formatTemp = input.getFormat();
        var format = resourceManager.getFormats().add(formatTemp);

        var formatEffectTemp = input.getFormatEffect();
        var formatEffect = resourceManager.getFormats().add(formatEffectTemp);

        return input.getText().chars()//
                .mapToObj(c -> (char) c)//
                .map(c -> TextElementFactory.fromChar(null, resourceManager, null, c, format, formatEffect))//
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public String write(TextData data) throws NotYetImplementedException {
        throw new NotYetImplementedException("JsonConverter: writeList is not implemented.");
    }

}
