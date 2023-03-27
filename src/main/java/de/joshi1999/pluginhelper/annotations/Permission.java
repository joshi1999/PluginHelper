package de.joshi1999.pluginhelper.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to determine the required permission for a command.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

  /**
   * Define the permission which is required to execute the command.
   *
   * @return permission which is required to execute the command
   */
  String[] value();

  /**
   * Define the message which will be sent to the executor if the command can't be executed because
   * of the missing permission. If the message is empty, the default message will be used.
   *
   * @return message which will be sent to the executor
   */
  String noPermissionMessage() default "";
}
