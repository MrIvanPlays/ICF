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
import com.mrivanplays.icf.external.DefaultArgumentResolvers;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;

public class CommandManager {

  private final BukkitCommandMapBridge mapBridge;
  private final Map<Class<?>, Function<String, ?>> argumentResolvers = new ConcurrentHashMap<>();
  private String noPermissionMessage;
  private String noConsoleMessage;

  public CommandManager(Plugin plugin) {
    mapBridge = new BukkitCommandMapBridge(plugin, this);
    new DefaultArgumentResolvers(this).registerAll();
    setNoPermissionMessage("&cYou don't have permission to perform this command");
    setNoConsoleMessage("&cThe command you've tried to run is player only.");
  }

  public void registerCommand(ICFCommand command, String... aliases) {
    mapBridge.registerCommand(command, aliases);
  }

  public <T> void registerArgumentResolver(Class<T> argumentClass, Function<String, T> function) {
    if (!argumentResolvers.containsKey(argumentClass)) {
      argumentResolvers.put(argumentClass, function);
    }
  }

  public Map<Class<?>, Function<String, ?>> getArgumentResolvers() {
    return ImmutableMap.copyOf(argumentResolvers);
  }

  public String getNoPermissionMessage() {
    return noPermissionMessage;
  }

  public void setNoPermissionMessage(String noPermissionMessage) {
    this.noPermissionMessage = colorize(noPermissionMessage);
  }

  public String getNoConsoleMessage() {
    return noConsoleMessage;
  }

  public void setNoConsoleMessage(String noConsoleMessage) {
    this.noConsoleMessage = colorize(noConsoleMessage);
  }

  private String colorize(String text) {
    return ChatColor.translateAlternateColorCodes('&', text);
  }
}
