package net.eugenpaul.jlexi.config;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import lombok.extern.slf4j.Slf4j;
import net.eugenpaul.jlexi.config.dto.GlobalConfigurationDto;

@Slf4j
public class GlobalConfiguration {
    private final ObjectMapper mapper;

    private GlobalConfigurationDto config;

    private String configPath;

    public GlobalConfiguration(String configPath) {
        this.mapper = new ObjectMapper();
        this.mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        this.configPath = configPath;
        this.config = null;
    }

    /**
     * Load/Reload configuration from file
     * 
     * @return true by success
     */
    public boolean load() {
        var file = new File(configPath);

        try {
            config = mapper.readValue(file, GlobalConfigurationDto.class);
            return true;
        } catch (IOException e) {
            LOGGER.error("Error by load the configuration", e);
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
        if (config == null) {
            LOGGER.error("Configuration is empty");
            return false;
        }

        var file = new File(configPath);

        try {
            ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
            writer.writeValue(file, config);
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
}
