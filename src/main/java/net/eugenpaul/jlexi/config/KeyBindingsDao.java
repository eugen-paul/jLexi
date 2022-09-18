package net.eugenpaul.jlexi.config;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.config.dto.KeyBindingsDto;
import net.eugenpaul.jlexi.config.dto.TextEditorKeysDto;

@Slf4j
public class KeyBindingsDao {

    private final ObjectMapper mapper;

    private String configPath;
    private KeyBindingsDto keyBindings;

    public KeyBindingsDao(String configPath) {
        this.mapper = new ObjectMapper();
        this.configPath = configPath;
        this.keyBindings = null;
    }

    /**
     * Load/Reload configuration from file
     * 
     * @return true by success
     */
    public boolean load() {
        var file = new File(configPath);

        try {
            keyBindings = mapper.readValue(file, KeyBindingsDto.class);
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

    public TextEditorKeysDto loadTextEditorKeys() {
        if (keyBindings == null) {
            return null;
        }

        return keyBindings.getTextEditor();
    }
}
