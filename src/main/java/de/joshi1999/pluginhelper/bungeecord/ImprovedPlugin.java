package de.joshi1999.pluginhelper.bungeecord;

import de.joshi1999.pluginhelper.bungeecord.config.ConfigurationManager;
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

  @Override
  public final void onEnable() {

  }

  /**
   * A prepared {@code Configuration} for the plugin, which is found in the plugin folder.
   *
   * @return the configuration
   */
  public Configuration getConfig() {
    return config;
  }
}
