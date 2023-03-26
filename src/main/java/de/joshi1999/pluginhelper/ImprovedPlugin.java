package de.joshi1999.pluginhelper;

import de.joshi1999.pluginhelper.config.ConfigurationManager;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;


public class ImprovedPlugin extends Plugin {
    private Configuration config;
    private final ConfigurationManager configurationManager;

    public ImprovedPlugin() {
        super();
        configurationManager = new ConfigurationManager(getDataFolder(), "config.yml", this);
        config = configurationManager.load();
    }

    /**
     * A prepared {@code Configuration} for the plugin, which is found in the plugin folder.
     * @return the configuration
     */
    public Configuration getConfig() {
        return config;
    }
}
