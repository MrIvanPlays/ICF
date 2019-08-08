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
package com.mrivanplays.icf.helpapi.external;

import com.mrivanplays.icf.CommandArguments;
import com.mrivanplays.icf.ICFCommand;
import com.mrivanplays.icf.TabCompleter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class CommandHelp extends ICFCommand implements TabCompleter {

  private final HelpPaginator paginator;
  private final String commandName;

  public CommandHelp(String commandName, String permission, Map<String, HelpEntry> commandHelp) {
    super(false, permission);
    paginator = new HelpPaginator(new ArrayList<>(commandHelp.entrySet()));
    this.commandName = commandName;
  }

  @Override
  public void execute(CommandSender sender, String label, CommandArguments args) {
    if (args.size() == 0) {
      List<Entry<String, HelpEntry>> commands = paginator.getPage(1);
      for (Entry<String, HelpEntry> entry : commands) {
        HelpEntry helpEntry = entry.getValue();
        sender.sendMessage(
            ChatColor.BLUE
                + "/"
                + entry.getKey()
                + " "
                + ChatColor.RESET
                + helpEntry.getUsage()
                + " - "
                + helpEntry.getDescription());
      }
      try {
        paginator.getPage(2);
        sender.sendMessage("Type /" + commandName + " help 2 for more help");
      } catch (IndexOutOfBoundsException ignored) {
      }
    }
    // todo: other stuff here
  }

  @Override
  public Iterable<String> tabComplete(CommandSender sender, String label, CommandArguments args) {
    return null;
  }
}
