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

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/** A utility class containing the default argument resolvers. */
public class ArgumentResolvers {

  public static ArgumentResolver<Integer> INTEGER = Integer::parseInt;

  /** @deprecated {@link CommandArguments#nextString()} has better performance */
  @Deprecated public static ArgumentResolver<String> STRING = input -> input;

  public static ArgumentResolver<Double> DOUBLE = Double::parseDouble;
  public static ArgumentResolver<Player> PLAYER = Bukkit::getPlayer;
  public static ArgumentResolver<Player> PLAYER_EXACT = Bukkit::getPlayerExact;
  public static ArgumentResolver<OfflinePlayer> PLAYER_OFFLINE = Bukkit::getOfflinePlayer;
}
