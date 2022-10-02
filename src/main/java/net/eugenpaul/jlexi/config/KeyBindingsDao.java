package net.eugenpaul.jlexi.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.config.dto.KeyBindingsDto;

@Slf4j
public class KeyBindingsDao {

    private final ObjectMapper mapper;

    private String configPath;
    private Map<String, KeyBindingsDto> keyBindings;

    public KeyBindingsDao(String configPath) {
        this.mapper = new ObjectMapper();
        this.mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        this.configPath = configPath;
        this.keyBindings = new HashMap<>();
    }

    /**
     * Load/Reload configuration from file
     * 
     * @return true by success
     */
    public boolean load() {
        var file = new File(configPath);

        try {
            List<KeyBindingsDto> data = mapper.readValue(file, new TypeReference<List<KeyBindingsDto>>() {
            });

            keyBindings = data.stream().collect(Collectors.toMap(KeyBindingsDto::getName, v -> v));

            return true;
        } catch (IOException e) {
            LOGGER.error("Error by load the keyBindings", e);
            return false;
        }
    }

    /**
     * Save current configuration to file
     * 
     * @param path - path to save
     * @return true by success
     */
    public boolean save(String path) {
        var file = new File(configPath);

        try {
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(file, keyBindings);
            return true;
        } catch (IOException e) {
            LOGGER.error("Error by writing the keyBindings", e);
            return false;
        }
    }

    /**
     * rewrite the configuration
     * 
     * @return true by success
     */
    public boolean save() {
        return save(configPath);
    }

    public KeyBindingsDto getKeys(String name) {
        return keyBindings.get(name);
    }
}
