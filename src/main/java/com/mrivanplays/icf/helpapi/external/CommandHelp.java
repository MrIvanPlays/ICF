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
import com.mrivanplays.icf.FailReason;
import com.mrivanplays.icf.ICFCommand;
import com.mrivanplays.icf.TabCompleter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class CommandHelp extends ICFCommand implements TabCompleter {

  private final HelpPaginator paginator;
  private final String commandName;
  private final Map<String, HelpEntry> entries;

  public CommandHelp(String commandName, String permission, Map<String, HelpEntry> commandHelp) {
    super(false, permission);
    paginator = new HelpPaginator(new ArrayList<>(commandHelp.entrySet()));
    this.commandName = commandName;
    entries = commandHelp;
  }

  @Override
  public void execute(CommandSender sender, String label, CommandArguments args) {
    if (args.size() == 0) {
      noArgRun(sender);
      return;
    }
    args.next(String.class)
        .ifPresent(
            subCommand -> {
              if (!subCommand.equalsIgnoreCase("help")) {
                return;
              }
              args.next(int.class)
                  .ifPresent(
                      page -> {
                        if (paginator.pageCount() < page) {
                          sender.sendMessage(
                              ChatColor.RED
                                  + "I can't find help page "
                                  + page
                                  + ". I think the last page is page "
                                  + paginator.pageCount());
                          return;
                        }
                        sendPage(sender, page);
                      })
                  .orElse(() -> noArgRun(sender));
            })
        .orElse(
            failReason -> {
              if (failReason == FailReason.ARGUMENT_PARSED_NOT_THE_TYPE) {
                sender.sendMessage(
                    ChatColor.RED + "You should parse a subcommand ex. /" + commandName + " help");
              }
            });
  }

  @Override
  public Iterable<String> tabComplete(CommandSender sender, String label, CommandArguments args) {
    Optional<String> firstArg = args.next();
    if (firstArg.isPresent()) {
      if (firstArg.get().equalsIgnoreCase("help")) {
        if (args.size() == 1) { // args.next removes the argument
          List<String> matches = new ArrayList<>();
          for (int i = 1; i < paginator.pageCount(); i++) {
            matches.add(Integer.toString(i));
          }
          return matches
              .parallelStream()
              .filter(match -> match.toLowerCase().startsWith(args.nextUnsafe().toLowerCase()))
              .collect(Collectors.toList());
        }
      }
    }
    return null;
  }

  private void noArgRun(CommandSender sender) {
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
    if (paginator.pageCount() > 1) {
      sender.sendMessage(ChatColor.BLUE + "Type /" + commandName + " help 2 for more help");
    }
  }

  private void sendPage(CommandSender sender, int page) {
    List<Entry<String, HelpEntry>> commands = paginator.getPage(page);
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
    if (paginator.pageCount() > page) {
      sender.sendMessage("Type /" + commandName + " help " + (page + 1) + " for more help");
    }
  }

  public boolean matches(CommandArguments args) {
    return args.nextUnsafe().equalsIgnoreCase("help");
  }

  public Map<String, HelpEntry> getHelpEntries() {
    return entries;
  }
}
