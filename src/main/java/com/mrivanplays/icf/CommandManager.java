/*
* Copyright 2019 Ivan Pekov (MrIvanPlays)
* Copyright 2019 contributors

* Permission is hereby granted, free of charge, to any person obtaining a copy of
* this software and associated documentation files (the "Software"), to deal in the
* Software without restriction, including without limitation the rights to use, copy,
* modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all copies
* or substantial portions of the Software.

* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
* OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
* ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
**/
package com.mrivanplays.icf;

import com.google.common.collect.ImmutableMap;
import com.mrivanplays.icf.external.BukkitCommandMapBridge;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/** Represents a manager of commands and argument resolvers. */
public final class CommandManager {

  private final BukkitCommandMapBridge mapBridge;
  private final Map<Class<?>, Function<String, ?>> argumentResolvers = new ConcurrentHashMap<>();
  private String noPermissionMessage;
  private String noConsoleMessage;

  public CommandManager(Plugin plugin) {
    mapBridge = new BukkitCommandMapBridge(plugin, this);
    argumentResolvers.put(String.class, input -> input); // default String argument
    argumentResolvers.put(
        int.class, // default integer argument
        input -> {
          try {
            return Integer.parseInt(input);
          } catch (NumberFormatException e) {
            return 0;
          }
        });
    argumentResolvers.put(
        double.class, // default double argument
        input -> {
          try {
            return Double.parseDouble(input);
          } catch (NumberFormatException e) {
            return 0.0;
          }
        });
    argumentResolvers.put(Player.class, Bukkit::getPlayer); // default player argument
    argumentResolvers.put(
        OfflinePlayer.class, Bukkit::getOfflinePlayer); // default offline player argument
    setNoPermissionMessage(
        "&cYou don't have permission to perform this command"); // default no permission message
    setNoConsoleMessage(
        "&cThe command you've tried to run is player only."); // default no console message
  }

  /**
   * Registers a new command.
   *
   * @param command the command you want to register
   * @param aliases the command names/aliases of which the command will get invoked
   * @param <T> command implementation type
   */
  public <T extends ICFCommand> void registerCommand(T command, String... aliases) {
    mapBridge.registerCommand(command, aliases);
  }

  /**
   * Registers a new argument resolver.
   *
   * @param argumentClass the argument's class you want to register a resolver
   * @param function a function which return value is the argument type and for a value to get
   *     transferred is the current argument.
   * @param <T> argument type
   * @deprecated in favour of {@link CommandArguments#next(Function)}
   */
  @Deprecated
  public <T> void registerArgumentResolver(Class<T> argumentClass, Function<String, T> function) {
    if (!argumentResolvers.containsKey(argumentClass)) {
      argumentResolvers.put(argumentClass, function);
    }
  }

  /**
   * Returns a unmodifiable copy of all registered argument resolvers. This also includes the
   * default registered argument resolvers in the constructor of this command manager.
   *
   * @return a map which is a unmodifiable copy of the original, containing all data about argument
   *     resolvers
   * @deprecated in favour of {@link CommandArguments#next(Function)}
   */
  @Deprecated
  public Map<Class<?>, Function<String, ?>> getArgumentResolvers() {
    return ImmutableMap.copyOf(argumentResolvers);
  }

  /**
   * Gets the no permission message, which is being used if the command sender does not have a
   * permission to invoke certain command. You are able to modify the default one with {@link
   * #setNoPermissionMessage(String)}
   *
   * @return no permission message
   */
  public String getNoPermissionMessage() {
    return noPermissionMessage;
  }

  /**
   * Sets a new no permission message
   *
   * @param noPermissionMessage new message
   * @see #getNoPermissionMessage()
   */
  public void setNoPermissionMessage(String noPermissionMessage) {
    this.noPermissionMessage = colorize(noPermissionMessage);
  }

  /**
   * Gets the no console message, which is being used if the invoked command is being player only.
   * You are able to modify the default one with {@link #setNoConsoleMessage(String)}
   *
   * @return no console message
   */
  public String getNoConsoleMessage() {
    return noConsoleMessage;
  }

  /**
   * Sets a new no console message
   *
   * @param noConsoleMessage new message
   * @see #getNoConsoleMessage()
   */
  public void setNoConsoleMessage(String noConsoleMessage) {
    this.noConsoleMessage = colorize(noConsoleMessage);
  }

  private String colorize(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }
}
