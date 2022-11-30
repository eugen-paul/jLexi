package net.eugenpaul.jlexi.component.text.converter.json;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.eugenpaul.jlexi.component.text.converter.TextConverterV2;
import net.eugenpaul.jlexi.component.text.converter.TextDataV2;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonElement;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonFooterV2;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonFormatV2;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonHeaderV2;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonParagraphV2;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonSectionV2;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactoryV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextElementV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextFooterDataV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextHeaderDataV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraphConfigurationV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextParagraphV2;
import net.eugenpaul.jlexi.component.text.format.structure.TextSectionConfiguration;
import net.eugenpaul.jlexi.component.text.format.structure.TextSectionV2;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class JsonConverterV2 implements TextConverterV2 {

    private final ObjectMapper mapper;

    private ResourceManager resourceManager;

    public JsonConverterV2(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.mapper = new ObjectMapper();
    }

    @Override
    public TextDataV2 read(String data) throws NotYetImplementedException {
        try {
            JsonFormatV2 value = mapper.readValue(data, JsonFormatV2.class);

            var header = toHeader(value.getHeader());
            var footer = toFooter(value.getFooter());
            var sections = value.getSections().stream()//
                    .map(this::toSection)//
                    .collect(Collectors.toCollection(LinkedList::new));

            return new TextDataV2(header, sections, footer);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error by parsing JSON-Data", e);
        }
    }

    private TextSectionV2 toSection(JsonSectionV2 input) {
        TextSectionV2 response = new TextSectionV2(null, input.getTextSectionConfiguration(), resourceManager);

        TextParagraphV2 lastParagraph = null;

        for (var paragraph : input.getParagraphs()) {
            lastParagraph = toParagraph(paragraph);
            lastParagraph.setParentStructure(response);
            response.add(lastParagraph);
        }

        if (lastParagraph == null) {
            TextParagraphV2 paragraph = new TextParagraphV2(//
                    null, //
                    TextParagraphConfigurationV2.builder().build(), //
                    resourceManager //
            );
            paragraph.setToEos(resourceManager);
            response.add(paragraph);
        } else {
            lastParagraph.setToEos(resourceManager);
        }

        return response;
    }

    private TextHeaderDataV2 toHeader(JsonHeaderV2 input) {
        if (input == null) {
            return null;
        }

        var headerSection = new TextSectionV2(null, TextSectionConfiguration.builder().block(true).build(),
                resourceManager);
        var responseHeader = new TextHeaderDataV2(headerSection, input.getConfiguration());

        TextParagraphV2 lastParagraph = null;

        for (var paragraph : input.getParagraphs()) {
            lastParagraph = toParagraph(paragraph);
            lastParagraph.setParentStructure(headerSection);
            headerSection.add(lastParagraph);
        }

        if (lastParagraph == null) {
            TextParagraphV2 paragraph = new TextParagraphV2(//
                    null, //
                    TextParagraphConfigurationV2.builder().build(), //
                    resourceManager //
            );
            paragraph.setToEos(resourceManager);
            headerSection.add(paragraph);
        } else {
            lastParagraph.setToEos(resourceManager);
        }

        return responseHeader;
    }

    private TextFooterDataV2 toFooter(JsonFooterV2 input) {
        if (input == null) {
            return null;
        }

        var headerSection = new TextSectionV2(null, TextSectionConfiguration.builder().block(true).build(),
                resourceManager);
        var responseHeader = new TextFooterDataV2(headerSection, input.getConfiguration());

        TextParagraphV2 lastParagraph = null;

        for (var paragraph : input.getParagraphs()) {
            lastParagraph = toParagraph(paragraph);
            lastParagraph.setParentStructure(headerSection);
            headerSection.add(lastParagraph);
        }

        if (lastParagraph == null) {
            TextParagraphV2 paragraph = new TextParagraphV2(//
                    null, //
                    TextParagraphConfigurationV2.builder().build(), //
                    resourceManager //
            );
            paragraph.setToEos(resourceManager);
            headerSection.add(paragraph);
        } else {
            lastParagraph.setToEos(resourceManager);
        }

        return responseHeader;
    }

    private TextParagraphV2 toParagraph(JsonParagraphV2 input) {
        TextParagraphV2 response = new TextParagraphV2(//
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

    private List<TextElementV2> toElement(JsonElement input) {
        var formatTemp = input.getFormat();
        var format = resourceManager.getFormats().add(formatTemp);

        var formatEffectTemp = input.getFormatEffect();
        var formatEffect = resourceManager.getFormats().add(formatEffectTemp);

        return input.getText().chars()//
                .mapToObj(c -> (char) c)//
                .map(c -> TextElementFactoryV2.fromChar(resourceManager, null, c, format, formatEffect))//
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public String write(TextDataV2 data) throws NotYetImplementedException {
        throw new NotYetImplementedException("JsonConverter: writeList is not implemented.");
    }

}
