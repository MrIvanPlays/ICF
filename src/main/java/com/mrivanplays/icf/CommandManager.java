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

import com.mrivanplays.icf.external.BukkitCommandMapBridge;
import com.mrivanplays.icf.external.CommandSendListener;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/** Represents a manager of commands and argument resolvers. */
public final class CommandManager {

  private final BukkitCommandMapBridge mapBridge;
  private String noPermissionMessage;
  private String noConsoleMessage;
  private final Plugin plugin;

  public CommandManager(@NotNull Plugin plugin) {
    this.plugin = plugin;
    mapBridge = new BukkitCommandMapBridge(plugin, this);
    setNoPermissionMessage(
        "&cYou don't have permission to perform this command"); // default no permission message
    setNoConsoleMessage(
        "&cThe command you've tried to run is player only."); // default no console message
  }

  /**
   * Registers a new {@link BaseCommand}.
   *
   * @param command the command you want to register
   */
  public void registerCommand(@NotNull BaseCommand command) {
    if (command.getAliases() != null) {
      String[] aliases = Arrays.copyOf(command.getAliases(), command.getAliases().length + 1);
      String firstAlias = aliases[0];
      aliases[0] = command.getName();
      aliases[command.getAliases().length + 1] = firstAlias;
      registerCommand(command, aliases);
    } else {
      registerCommand(command, command.getName());
    }
  }

  /**
   * Registers a new command.
   *
   * @param command the command you want to register
   * @param aliases the command names/aliases of which the command will get invoked
   */
  public void registerCommand(@NotNull ICFCommand command, @NotNull String... aliases) {
    mapBridge.registerCommand(command, aliases);
  }

  /**
   * Enables permission check for showing up commands, registered by this manager by doing /[tab]
   * ingame. It is suggested to call this method after registering all commands you have, because if
   * you call it directly after the manager was initialized, it will not take effect.
   *
   * <p>It was not our fault that the commands were added to the tab completion even if the player
   * had no permission to view them, that's bukkit's permission system fault.
   */
  public void enablePermissionCheckWhenFirstTabComplete() {
    Bukkit.getPluginManager()
        .registerEvents(new CommandSendListener(plugin.getName(), mapBridge.getCommands()), plugin);
  }

  /**
   * Gets the no permission message, which is being used if the command sender does not have a
   * permission to invoke certain command. You are able to modify the default one with {@link
   * #setNoPermissionMessage(String)}
   *
   * @return no permission message
   */
  @NotNull
  public String getNoPermissionMessage() {
    return noPermissionMessage;
  }

  /**
   * Sets a new no permission message
   *
   * @param noPermissionMessage new message
   * @see #getNoPermissionMessage()
   */
  public void setNoPermissionMessage(@NotNull String noPermissionMessage) {
    this.noPermissionMessage = colorize(noPermissionMessage);
  }

  /**
   * Gets the no console message, which is being used if the invoked command is being player only.
   * You are able to modify the default one with {@link #setNoConsoleMessage(String)}
   *
   * @return no console message
   */
  @NotNull
  public String getNoConsoleMessage() {
    return noConsoleMessage;
  }

  /**
   * Sets a new no console message
   *
   * @param noConsoleMessage new message
   * @see #getNoConsoleMessage()
   */
  public void setNoConsoleMessage(@NotNull String noConsoleMessage) {
    this.noConsoleMessage = colorize(noConsoleMessage);
  }

  private String colorize(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }
}
