package de.joshi1999.pluginhelper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to declare a class as a command handling class. At default, only players
 * can execute the command. After the successful execution of the command, a message will be sent to
 * the executor, only and only if the afterExecuteMessage parameter is not empty.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandExecutor {

  /**
   * Define the name of the command.
   *
   * @return name of the command
   */
  String value();

  /**
   * Define if the command can be executed by the console.
   *
   * @return true if the command can be executed by the console
   */
  boolean console() default true;

  /**
   * Define the message which will be sent to the executor after the successful execution of the
   * command. If the message is empty, no message will be sent.
   *
   * @return message which will be sent to the executor
   */
  String afterExecuteMessage() default "";

  /**
   * Define the message which will be sent to the executor if the command can't be executed by the
   * console. If the message is empty, the default message will be used.
   *
   * @return message which will be sent to the executor
   */
  String noConsoleMessage() default "";
}
