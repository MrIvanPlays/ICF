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

import org.bukkit.command.CommandSender;

/** Represents a command, which is made for our purposes. */
public abstract class ICFCommand {

  private boolean playerOnly;
  private String permission;

  public ICFCommand() {
    this(false);
  }

  public ICFCommand(boolean playerOnly) {
    this(playerOnly, null);
  }

  public ICFCommand(boolean playerOnly, String permission) {
    this.playerOnly = playerOnly;
    this.permission = permission;
  }

  /**
   * Returns whenever this command is player only.
   *
   * @return <code>true</code> if player only, otherwise <code>false</code>
   */
  public boolean isPlayerOnly() {
    return playerOnly;
  }

  /**
   * Sets if this command should be player only.
   *
   * @param playerOnly value
   */
  public void setPlayerOnly(boolean playerOnly) {
    this.playerOnly = playerOnly;
  }

  /**
   * Gets the permission of this command.
   *
   * @return permission, may be null
   */
  public String getPermission() {
    return permission;
  }

  /**
   * Sets a new permission for this command.
   *
   * @param permission permission
   */
  public void setPermission(String permission) {
    this.permission = permission;
  }

  /**
   * Returns whenever the {@link CommandSender} has permission to execute this command.
   *
   * @param sender the sender you wish to check if has a permission
   * @return <code>true</code> if has permission, otherwise <code>false</code>
   */
  public boolean hasPermission(CommandSender sender) {
    return permission == null || permission.isEmpty() || sender.hasPermission(permission);
  }

  /**
   * Executes the command when it is being invoked.
   *
   * @param sender the sender which invoked this command
   * @param label the label of which the command got invoked
   * @param args the arguments with which the command got invoked
   */
  public abstract void execute(CommandSender sender, String label, CommandArguments args);
}
