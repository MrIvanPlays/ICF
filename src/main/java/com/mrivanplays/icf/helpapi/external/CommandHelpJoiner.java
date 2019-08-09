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
import org.bukkit.command.CommandSender;

public class CommandHelpJoiner extends ICFCommand implements TabCompleter {

  private final CommandHelp helpCommand;
  private final ICFCommand otherCommand;

  public CommandHelpJoiner(CommandHelp helpCommand, ICFCommand otherCommand) {
    super(otherCommand.isPlayerOnly(), otherCommand.getPermission());
    this.helpCommand = helpCommand;
    this.otherCommand = otherCommand;
  }

  @Override
  public void execute(CommandSender sender, String label, CommandArguments args) {
    if (args.size() == 0) {
      helpCommand.execute(sender, label, args);
      return;
    }
    String subcommand = args.nextUnsafe();
    if (subcommand.equalsIgnoreCase("help")) {
      helpCommand.execute(sender, label, args);
      return;
    }
    otherCommand.execute(sender, label, args);
  }

  @Override
  public Iterable<String> tabComplete(CommandSender sender, String label, CommandArguments args) {
    if (otherCommand instanceof TabCompleter) {
      TabCompleter otherCommandCompleter = (TabCompleter) otherCommand;
      if (args.size() == 0) {
        return otherCommandCompleter.tabComplete(sender, label, args);
      }
      String subArgument = args.nextUnsafe();
      if (subArgument.equalsIgnoreCase("help")) {
        return helpCommand.tabComplete(sender, label, args);
      }
      return otherCommandCompleter.tabComplete(sender, label, args);
    }
    return null;
  }
}
