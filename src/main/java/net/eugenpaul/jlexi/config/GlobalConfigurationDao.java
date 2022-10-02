package net.eugenpaul.jlexi.config;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.config.dto.GlobalConfigurationDto;
import net.eugenpaul.jlexi.config.dto.KeyBindingsDto;

@Slf4j
public class GlobalConfigurationDao {
    private final ObjectMapper mapper;
    private final ObjectWriter writer;

    private GlobalConfigurationDto config;

    private KeyBindingsDao keys;

    private String configPath;

    public GlobalConfigurationDao(String configPath) {
        this.mapper = new ObjectMapper();
        this.mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        this.writer = mapper.writer(new DefaultPrettyPrinter());

        this.configPath = configPath;
        this.config = null;
        this.keys = null;
    }

    /**
     * Load/Reload configuration from file
     * 
     * @return true by success
     * @throws IOException
     */
    public void load() throws IOException {
        var file = new File(this.configPath);

        try {
            this.config = mapper.readValue(file, GlobalConfigurationDto.class);

            this.keys = new KeyBindingsDao(this.config.getKeyBindingsPath());
            this.keys.load();
        } catch (IOException e) {
            LOGGER.error("Error by load the configuration", e);
            this.config = null;
            this.keys = null;
            throw e;
        }
    }

    /**
     * Save current configuration to file
     * 
     * @param path - path to save
     * @return true by success
     */
    public boolean save(String path) {
        if (this.config == null) {
            LOGGER.error("Configuration is empty");
            return false;
        }

        var file = new File(this.configPath);

        try {
            this.writer.writeValue(file, this.config);
            return true;
        } catch (IOException e) {
            LOGGER.error("Error by writing the configuration", e);
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
        if (keys == null) {
            return null;
        }

        return keys.getKeys(name);
    }
}
