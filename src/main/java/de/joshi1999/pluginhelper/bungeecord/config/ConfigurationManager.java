package de.joshi1999.pluginhelper.bungeecord.config;

import de.joshi1999.pluginhelper.bungeecord.ImprovedPlugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public record ConfigurationManager(File dataFolder, String fileName, ImprovedPlugin improvedPlugin) {

    /**
     * Loads a {@code Configuration} from "config.yml" in the plugin's folder and returns it.
     * If there is no config.yml in the plugin's folder, it will be created from the resources.
     * @return a filled {@code Configuration}
     */
    public Configuration load() {
        Configuration config = null;
        try {
            if (!dataFolder.exists()) {
                dataFolder.mkdirs();
            }

            File file = new File(dataFolder, fileName);

            if (!file.exists()) {
                improvedPlugin.getLogger().info(String.format("%s not found, creating new one!", fileName));
                try (InputStream in = improvedPlugin.getResourceAsStream("config.yml")) {
                    Files.copy(in, file.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                improvedPlugin.getLogger().info(String.format("%s found, loading!", fileName));
            }
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(dataFolder, fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    /**
     * Saves the given {@code Configuration} to the config.yml in the plugin's folder. If the folder does not exist,
     * it will be created.
     * @param config the configuration which shall be saved.
     */
    @SuppressWarnings("unused")
    public void save(Configuration config) {
        try {
            if (!new File(dataFolder, fileName).getParentFile().exists()) {
                new File(dataFolder, fileName).mkdirs();
            }
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(dataFolder, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}