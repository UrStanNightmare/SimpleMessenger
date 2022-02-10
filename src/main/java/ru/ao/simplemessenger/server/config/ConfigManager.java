package ru.ao.simplemessenger.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final static Logger log = LoggerFactory.getLogger(ConfigManager.class.getName());

    private final static String CONFIG_FILE_PATH = "cfg.json";

    private final static int DEFAULT_PORT = 25565;

    private final ObjectMapper mapper = new ObjectMapper();
    private final File configFile = new File(CONFIG_FILE_PATH);
    private ServerConfigDTO configs;

    public ConfigManager() throws IOException {
        try {
            this.configs = mapper.readValue(configFile, ServerConfigDTO.class);
        } catch (IOException e) {
            this.createConfigFile();
            throw new IOException("Config file error! " + e.getMessage());
        }
    }

    public int getPort() throws NumberFormatException, IOException {
        if (configs.getPort() == null) {
            this.createConfigFile();
            throw new IOException("Port not specified in config file! Config will be recreated!");
        }

        return Integer.parseInt(configs.getPort());
    }

    private void createConfigFile() {
        try {
            mapper.writeValue(configFile, new ServerConfigDTO("25565"));
        } catch (IOException e) {
            log.error("An attempt to create default config file failed! {}", e.getMessage());
        }
    }
}
