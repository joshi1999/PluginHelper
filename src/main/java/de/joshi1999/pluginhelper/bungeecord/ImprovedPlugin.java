package de.joshi1999.pluginhelper.bungeecord;

import de.joshi1999.pluginhelper.annotations.Registrable;
import de.joshi1999.pluginhelper.bungeecord.config.ConfigurationManager;
import de.joshi1999.pluginhelper.utils.ClassAnalyzer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;


public abstract class ImprovedPlugin extends Plugin {

  private Configuration config;
  private ConfigurationManager configurationManager;

  @Override
  public final void onLoad() {
    configurationManager = new ConfigurationManager(getDataFolder(), "config.yml", this);
    config = configurationManager.load();

    onPluginLoad();
  }

  @Override
  public final void onEnable() {
    // Register all commands
    for (Class<?> commandClass : ClassAnalyzer.findClassesByAnnotation(getClass().getPackageName(), Registrable.class)) {
      try {
        Command command = (Command) commandClass.getConstructor(ImprovedPlugin.class).newInstance(this);
        getProxy().getPluginManager().registerCommand(this, command);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    onPluginEnable();
  }

  /**
   * This method is called when the plugin is enabled.
   */
  protected void onPluginEnable() {

  }

  /**
   * This method is called when the plugin is loaded.
   */
  protected void onPluginLoad() {

  }

  /**
   * A prepared {@code Configuration} for the plugin, which is found in the plugin folder.
   *
   * @return the configuration
   */
  public Configuration getConfig() {
    return config;
  }

  /**
   * Loads the configuration.
   */
  public void loadConfig() {
    config = configurationManager.load();
  }
}
