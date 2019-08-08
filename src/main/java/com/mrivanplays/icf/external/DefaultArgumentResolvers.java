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
package com.mrivanplays.icf.external;

import com.mrivanplays.icf.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class DefaultArgumentResolvers {

  private final CommandManager commandManager;

  public DefaultArgumentResolvers(CommandManager commandManager) {
    this.commandManager = commandManager;
  }

  public void registerAll() {
    commandManager.registerArgumentResolver(String.class, input -> input);
    commandManager.registerArgumentResolver(
        int.class,
        input -> {
          try {
            return Integer.parseInt(input);
          } catch (NumberFormatException e) {
            return 0;
          }
        });
    commandManager.registerArgumentResolver(
        double.class,
        input -> {
          try {
            return Double.parseDouble(input);
          } catch (NumberFormatException e) {
            return 0.0;
          }
        });
    commandManager.registerArgumentResolver(Player.class, Bukkit::getPlayer);
    commandManager.registerArgumentResolver(OfflinePlayer.class, Bukkit::getOfflinePlayer);
  }
}
