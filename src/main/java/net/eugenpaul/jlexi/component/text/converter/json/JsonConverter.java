package net.eugenpaul.jlexi.component.text.converter.json;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.eugenpaul.jlexi.component.text.converter.TextConverter;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonElement;
import net.eugenpaul.jlexi.component.text.converter.json.format.JsonFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextElementFactory;
import net.eugenpaul.jlexi.exception.NotYetImplementedException;
import net.eugenpaul.jlexi.resourcesmanager.ResourceManager;

public class JsonConverter implements TextConverter {

    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonConverter.class);

    private final ObjectMapper mapper;

    private ResourceManager resourceManager;

    public JsonConverter(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
        this.mapper = new ObjectMapper();
    }

    @Override
    public List<TextElement> read(String data) throws NotYetImplementedException {
        List<TextElement> response = new LinkedList<>();

        try {
            JsonFormat value = mapper.readValue(data, JsonFormat.class);

            value.getData().stream()//
                    .map(this::toElementList)//
                    .forEach(response::addAll);

        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error by parsing JSON-Data", e);
        }

        return response;
    }

    private List<TextElement> toElementList(JsonElement input) {
        var formatTemp = input.getFormat();
        var format = resourceManager.getFormats().add(formatTemp);

        var formatEffectTemp = input.getFormatEffect();
        var formatEffect = resourceManager.getFormats().add(formatEffectTemp);

        var response = input.getText().chars()//
                .mapToObj(c -> (char) c)//
                .map(c -> TextElementFactory.fromChar(null, resourceManager, null, c, format, formatEffect))//
                .collect(Collectors.toCollection(LinkedList::new));

        if (input.isEol()) {
            response.add(TextElementFactory.genNewLineChar(null, resourceManager, null, format, formatEffect));
        }
        if (input.isEos()) {
            response.add(TextElementFactory.genNewSectionChar(null, resourceManager, null, format, formatEffect));
        }

        return response;
    }

    @Override
    public String write(List<TextElement> data) throws NotYetImplementedException {
        throw new NotYetImplementedException("JsonConverter: writeList is not implemented.");
    }

    @Override
    public String write(Iterator<TextElement> data) throws NotYetImplementedException {
        throw new NotYetImplementedException("JsonConverter: writeIterator is not implemented.");
    }

}
