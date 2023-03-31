package de.joshi1999.pluginhelper.bungeecord;

import de.joshi1999.pluginhelper.annotations.CommandExecutor;
import de.joshi1999.pluginhelper.annotations.Permission;
import java.lang.reflect.Method;
import net.md_5.bungee.api.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * Command class. This class helps to execute Commands.
 */
@SuppressWarnings("unused")
public abstract class Command extends net.md_5.bungee.api.plugin.Command {

  private String noPermissionMessage = "You don't have the permission to execute this command.";
  private String noConsoleMessage = "This command can only be executed by a player.";
  private final ImprovedPlugin plugin;

  /**
   * Constructor for a command. Default messages will be loaded from config file.
   * <p>
   * The config file must be named "config.yml" and must be located in the plugin folder. The
   * default messages are:
   * <ul>
   *   <li>noPermissionMessage: You don't have the permission to execute this command.</li>
   *   <li>noConsoleMessage: This command can only be executed by a player.</li>
   * </ul>
   * If you want to change the default messages, you can do this by adding the following lines to
   * your config file:
   * <pre>
   *   messages:
   *     noPermissionMessage: You don't have the permission to execute this command.
   *     noConsoleMessage: This command can only be executed by a player.
   * </pre>
   *
   * @param plugin plugin which is using this command
   * @param name   name of the command
   */
  public Command(@NotNull ImprovedPlugin plugin, @NotNull String name) {
    super(name);
    this.plugin = plugin;
    setDefaultMessages();
  }

  /**
   * Executes all annotated command handling methods with the given command name. Throws a
   * RuntimeException if no method was found. Throws a RuntimeException if the return type of one
   * method is not boolean.
   *
   * @param sender      The sender of the command
   * @param args        String[] of arguments
   * @param commandName name of the subcommand which should be executed
   */
  @SuppressWarnings("unused")
  public final void executeCommand(@NotNull CommandSender sender, @NotNull String[] args,
      @NotNull String commandName) {
    int count = 0;
    for (Method method : getClass().getDeclaredMethods()) {
      if (!method.isAnnotationPresent(CommandExecutor.class)) {
        continue;
      }
      count++;

      if (!method.getAnnotation(CommandExecutor.class).value().equals(commandName)) {
        continue;
      }
      try {

        if (!method.getAnnotation(CommandExecutor.class).console() && !(sender instanceof
            net.md_5.bungee.api.connection.ProxiedPlayer)) {
          if (!method.getAnnotation(CommandExecutor.class).noConsoleMessage().isEmpty()) {
            String configPath = method.getAnnotation(CommandExecutor.class).noConsoleMessage();
            String message = plugin.getConfig().getString(configPath);
            sender.sendMessage(message);
          } else {
            sender.sendMessage(noConsoleMessage);
          }
          continue;
        }

        if (method.isAnnotationPresent(Permission.class)) {
          boolean hasPermission = false;
          for (String permission : method.getAnnotation(Permission.class).value()) {
            if (sender.hasPermission(permission)) {
              hasPermission = true;
              break;
            }
          }
          if (!hasPermission) {
            if (!method.getAnnotation(Permission.class).noPermissionMessage().isEmpty()) {
              String configPath = method.getAnnotation(Permission.class).noPermissionMessage();
              String message = plugin.getConfig().getString(configPath);
              sender.sendMessage(message);
            } else {
              sender.sendMessage(noPermissionMessage);
            }
            continue;
          }
        }

        method.setAccessible(true);
        method.invoke(this, sender, args);

        if (!method.getAnnotation(CommandExecutor.class).afterExecuteMessage().isEmpty()) {
          String configPath = method.getAnnotation(CommandExecutor.class).afterExecuteMessage();
          String message = plugin.getConfig().getString(configPath);
          sender.sendMessage(message);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (count == 0) {
      throw new RuntimeException("No command processing method found.");
    }
  }

  /**
   * Setting default message which will be sent to the sender if they don't have the permission to
   * execute the command.
   *
   * @param message message which will be sent to the sender
   */
  @SuppressWarnings("unused")
  public final void setNoPermissionMessage(@NotNull String message) {
    noPermissionMessage = message;
  }

  /**
   * Setting default message which will be sent to the sender if they try to execute the command
   * from the console.
   *
   * @param message message which will be sent to the sender
   */
  @SuppressWarnings("unused")
  public final void setNoConsoleMessage(@NotNull String message) {
    noConsoleMessage = message;
  }

  private void setDefaultMessages() {
    noPermissionMessage = plugin.getConfig()
        .getString("messages.noPermission", noPermissionMessage);
    noConsoleMessage = plugin.getConfig()
        .getString("messages.noConsole", noConsoleMessage);
  }
}
