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
import com.mrivanplays.icf.helpapi.external.AnnotationFinder;
import com.mrivanplays.icf.helpapi.external.CommandHelp;
import com.mrivanplays.icf.helpapi.external.CommandHelpJoiner;
import com.mrivanplays.icf.helpapi.external.HelpEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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
  private boolean helpCommandEnabled;
  private String helpBaseCommand;
  private final Map<String, Entry<Optional<String>, Optional<String>>> commandHelp =
      new ConcurrentHashMap<>();

  public CommandManager(Plugin plugin) {
    mapBridge = new BukkitCommandMapBridge(plugin, this);
    registerArgumentResolver(String.class, input -> input); // default String argument
    registerArgumentResolver(
        int.class, // default integer argument
        input -> {
          try {
            return Integer.parseInt(input);
          } catch (NumberFormatException e) {
            return 0;
          }
        });
    registerArgumentResolver(
        double.class, // default double argument
        input -> {
          try {
            return Double.parseDouble(input);
          } catch (NumberFormatException e) {
            return 0.0;
          }
        });
    registerArgumentResolver(Player.class, Bukkit::getPlayer); // default player argument
    registerArgumentResolver(
        OfflinePlayer.class, Bukkit::getOfflinePlayer); // default offline player argument
    setNoPermissionMessage(
        "&cYou don't have permission to perform this command"); // default no permission message
    setNoConsoleMessage(
        "&cThe command you've tried to run is player only."); // default no console message
    helpCommandEnabled = false;
  }

  /**
   * Enables the help command. It is generally preferable and suggested to make the base command a
   * different (ex. mypluginhelp) as if you have another commands at /myplugin this could bug
   * everything. This API is still not that stable and you may experience some errors. If you have
   * any errors using this open a PR with fix or notify MrIvanPlays with the errors.
   *
   * @param baseCommand the command base to get triggered. ex /myplugin [help] ...
   * @param permission the permission required to invoke the command
   * @param usageColor the color of the command name and usage
   * @param descriptionColor the color of the description
   * @throws IllegalArgumentException if a command does not have a syntax or description
   */
  public void enableHelp(
      String baseCommand, String permission, ChatColor usageColor, ChatColor descriptionColor) {
    helpCommandEnabled = true;
    helpBaseCommand = baseCommand;
    Map<String, HelpEntry> entries = new ConcurrentHashMap<>();
    for (Entry<String, Entry<Optional<String>, Optional<String>>> entry : commandHelp.entrySet()) {
      String commandName = entry.getKey();
      Entry<Optional<String>, Optional<String>> helpEntryRaw = entry.getValue();
      Optional<String> descriptionOptional = helpEntryRaw.getKey();
      Optional<String> syntaxOptional = helpEntryRaw.getValue();
      if (!syntaxOptional.isPresent()) {
        throw new IllegalArgumentException("Command '" + commandName + "' does not have a syntax");
      }
      if (!descriptionOptional.isPresent()) {
        throw new IllegalArgumentException(
            "Command '" + commandName + "' does not have a description");
      }
      entries.put(
          commandName,
          new HelpEntry(
              descriptionColor + descriptionOptional.get(), usageColor + syntaxOptional.get()));
    }
    registerCommand(new CommandHelp(baseCommand, permission, entries));
  }

  /**
   * Registers a new command.
   *
   * @param command the command you want to register
   * @param aliases the command names/aliases of which the command will get invoked
   * @param <T> command implementation type
   */
  public <T extends ICFCommand> void registerCommand(T command, String... aliases) {
    String commandName = aliases[0];
    if (helpCommandEnabled) {
      if (!(command instanceof CommandHelp)) {
        Optional<String> description = AnnotationFinder.findDescription(command);
        Optional<String> syntax = AnnotationFinder.findSyntax(command);
        if (!commandHelp.containsKey(commandName)) {
          commandHelp.put(commandName, new ConcurrentHashMap.SimpleEntry<>(description, syntax));
        }
      } else {
        CommandHelp help = (CommandHelp) command;
        Map<String, HelpEntry> helpEntries = help.getHelpEntries();
        if (helpEntries.containsKey(commandName)) {
          mapBridge.registerCommand(
              new CommandHelpJoiner(help, mapBridge.getCommand(commandName).get()));
        }
      }
    }
    mapBridge.registerCommand(command, aliases);
  }

  /**
   * Registers a new argument resolver.
   *
   * @param argumentClass the argument's class you want to register a resolver
   * @param function a function which return value is the argument type and for a value to get
   *     transferred is the current argument.
   * @param <T> argument type
   */
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
   */
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
