/*
    Copyright (c) 2019 Ivan Pekov
    Copyright (c) 2019 Contributors

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
*/
package com.mrivanplays.icf;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Represents a normal {@link ICFCommand} with a storage of its name and aliases. */
public abstract class BaseCommand extends ICFCommand {

  private final String name;
  private String[] aliases;

  public BaseCommand(@NotNull String name) {
    this.name = name;
  }

  public BaseCommand(boolean playerOnly, @NotNull String name) {
    this(playerOnly, null, name);
  }

  public BaseCommand(boolean playerOnly, @Nullable String permission, @NotNull String name) {
    super(playerOnly, permission);
    this.name = name;
  }

  public BaseCommand(@NotNull String name, @Nullable String... aliases) {
    this.name = name;
    this.aliases = aliases;
  }

  public BaseCommand(boolean playerOnly, @NotNull String name, @Nullable String... aliases) {
    this(playerOnly, null, name, aliases);
  }

  public BaseCommand(
      boolean playerOnly,
      @Nullable String permission,
      @NotNull String name,
      @Nullable String... aliases) {
    super(playerOnly, permission);
    this.name = name;
    this.aliases = aliases;
  }

  /**
   * Returns the name of the command.
   *
   * @return name
   */
  @NotNull
  public String getName() {
    return name;
  }

  /**
   * Returns the aliases of the command.
   *
   * @return aliases
   */
  @Nullable
  public String[] getAliases() {
    return aliases;
  }
}
