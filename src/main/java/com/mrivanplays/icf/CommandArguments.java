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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CommandArguments {

  private final Map<Class<?>, Function<String, ?>> argumentResolvers;
  private final List<String> args;

  public CommandArguments(CommandManager commandManager, String[] args) {
    argumentResolvers = commandManager.getArgumentResolvers();
    this.args = Arrays.asList(args);
  }

  public <T> ArgumentOptional<T> next(Class<T> argumentClass) {
    if (argumentResolvers.containsKey(argumentClass) || args.size() == 0) {
      return ArgumentOptional.of(null);
    }
    Function<String, ?> resolver = argumentResolvers.get(argumentClass);
    String arg = args.remove(0);
    T resolved = (T) resolver.apply(arg);
    if (argumentClass.isAssignableFrom(int.class)) {
      if (((int) resolved) == 0) {
        return ArgumentOptional.of(null);
      }
    }
    if (argumentClass.isAssignableFrom(double.class)) {
      if (((double) resolved) == 0.0) {
        return ArgumentOptional.of(null);
      }
    }
    return ArgumentOptional.of(resolved);
  }

  public int size() {
    return args.size();
  }
}
