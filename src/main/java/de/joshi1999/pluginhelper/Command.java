package de.joshi1999.pluginhelper;

import de.joshi1999.pluginhelper.annotations.CommandExecutor;
import de.joshi1999.pluginhelper.annotations.Permission;
import java.lang.reflect.Method;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Command class. This class helps to execute Commands.
 */
public abstract class Command extends net.md_5.bungee.api.plugin.Command {

  private String noPermissionMessage = "You don't have the permission to execute this command.";
  private Plugin plugin;

  /**
   * Default Constructor for a command. If the given plugin is an instance of ImprovedPlugin,
   * required messages will be loaded from the config.yml file. Otherwise, the default messages will
   * be used.
   * @param plugin plugin which is using this command
   * @param name name of the command
   */
  public Command(Plugin plugin, String name) {
    super(name);
    this.plugin = plugin;
    setDefaultMessages();
  }

  /**
   * Default Constructor for a command. Default messages will be used.
   * @param name name of the command
   */
  public Command(String name) {
    super(name);
    setDefaultMessages();
  }

  /**
   * Executes all annotated command handling methods with the given command name.
   * Throws a RuntimeException if no method was found.
   * Throws a RuntimeException if the return type of one method is not boolean.
   * @param sender The sender of the command
   * @param args String[] of arguments
   * @param commandName name of the command which should be executed
   * @return true if the command was executed successfully by each invoked method
   */
  public final boolean executeCommand(CommandSender sender, String[] args, String commandName) {
    boolean result = true;
    int count = 0;
    for (Method method : getClass().getMethods()) {
      if (method.isAnnotationPresent(CommandExecutor.class)) {
        count++;
        if (method.getAnnotation(CommandExecutor.class).value().equals(commandName)) {
          try {
            if (method.getReturnType() == boolean.class) {
              if (method.isAnnotationPresent(Permission.class)) {
                if (!sender.hasPermission(method.getAnnotation(Permission.class).value())) {
                  sender.sendMessage(noPermissionMessage);
                  continue;
                }
              }
              boolean b = (boolean) method.invoke(this, sender, args);
              if (!b) {
                result = false;
              }
            } else {
              throw new RuntimeException("Command processing method must return boolean.");
            }
            result = (boolean) method.invoke(this, sender, args);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
    if (count == 0) {
      throw new RuntimeException("No command processing method found.");
    }
    return result;
  }

  /**
   * Setting default message which will be sent to the sender if they don't have the permission to
   * execute the command.
   * @param message message which will be sent to the sender
   */
  public final void setNoPermissionMessage(String message) {
    noPermissionMessage = message;
  }

  private void setDefaultMessages() {
    if (plugin instanceof ImprovedPlugin) {
      noPermissionMessage = ((ImprovedPlugin) plugin).getConfig()
          .getString("messages.noPermission", noPermissionMessage);
    }
  }
}
