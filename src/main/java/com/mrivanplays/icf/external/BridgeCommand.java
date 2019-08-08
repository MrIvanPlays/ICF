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

import com.google.common.collect.ImmutableList;
import com.mrivanplays.icf.CommandArguments;
import com.mrivanplays.icf.CommandManager;
import com.mrivanplays.icf.ICFCommand;
import com.mrivanplays.icf.TabCompleter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BridgeCommand extends Command {

  private final ICFCommand icfCommand;
  private final CommandManager commandManager;

  protected BridgeCommand(ICFCommand icfCommand, CommandManager commandManager, String... aliases) {
    super(aliases[0]);
    setAliases(Arrays.asList(Arrays.copyOfRange(aliases, 1, aliases.length)));
    this.icfCommand = icfCommand;
    this.commandManager = commandManager;
  }

  @Override
  public boolean execute(CommandSender sender, String commandLabel, String[] args) {
    if (icfCommand.isPlayerOnly()) {
      if (!(sender instanceof Player)) {
        sender.sendMessage(commandManager.getNoConsoleMessage());
        return true;
      }
    }
    if (!icfCommand.hasPermission(sender)) {
      sender.sendMessage(commandManager.getNoPermissionMessage());
      return true;
    }
    icfCommand.execute(sender, commandLabel, new CommandArguments(commandManager, args));
    return true;
  }

  @Override
  public List<String> tabComplete(CommandSender sender, String label, String[] args) {
    if (!(icfCommand instanceof TabCompleter)) {
      return super.tabComplete(sender, label, args);
    }
    if (!icfCommand.hasPermission(sender)) {
      return Collections.emptyList();
    }
    return ImmutableList.copyOf(
        ((TabCompleter) icfCommand)
            .tabComplete(sender, label, new CommandArguments(commandManager, args)));
  }
}
