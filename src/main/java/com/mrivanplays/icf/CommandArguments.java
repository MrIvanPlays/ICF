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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/** Represents a class, containing the actual argument resolving of a command. */
public final class CommandArguments {

  private final Map<Class<?>, Function<String, ?>> argumentResolvers;
  private final List<String> args;

  public CommandArguments(CommandManager commandManager, String[] args) {
    argumentResolvers = commandManager.getArgumentResolvers();
    this.args = new ArrayList<>(Arrays.asList(args));
  }

  /**
   * Gets the next argument. This method is unsafe as the method may return null if there are no
   * arguments and the safe alternatives of this method are {@link #next()} or {@link #next(Class)}
   *
   * @return a string argument or null
   */
  public String nextUnsafe() {
    return getArgUnsafe(0);
  }

  /**
   * Gets the next argument.
   *
   * @return a optional with string argument if present, a empty optional if not present
   */
  public Optional<String> next() {
    return Optional.ofNullable(nextUnsafe());
  }

  /**
   * Gets the specified argument. This method is unsafe as the method may return null if there are
   * no arguments and the safe alternative of this method is {@link #getArg(int)}
   *
   * @param arg the argument you want to get
   * @return the string argument or null
   */
  public String getArgUnsafe(int arg) {
    if (args.size() == 0) {
      return null;
    }
    return args.remove(arg);
  }

  /**
   * Gets the specified argument.
   *
   * @param arg the argument yiu want to get
   * @return a optional with string argument if present, a empty optional if not present
   */
  public Optional<String> getArg(int arg) {
    return Optional.ofNullable(getArgUnsafe(arg));
  }

  /**
   * Gets the next argument, resolving it to the specified class.
   *
   * @param argumentClass the argument's class you wish to resolve the argument to
   * @param <T> new argument type
   * @return empty {@link ArgumentOptional} if there's no argument resolver for this class, there
   *     are no arguments or the resolved argument is null.
   */
  public <T> ArgumentOptional<T> next(Class<T> argumentClass) {
    if (!argumentResolvers.containsKey(argumentClass)) {
      return ArgumentOptional.of(null, FailReason.ARGUMENT_RESOLVER_NOT_FOUND);
    }
    if (args.size() == 0) {
      return ArgumentOptional.of(null, FailReason.ARGUMENT_NOT_PARSED);
    }
    Function<String, ?> resolver = argumentResolvers.get(argumentClass);
    String arg = nextUnsafe();
    T resolved = (T) resolver.apply(arg);
    if (argumentClass.isAssignableFrom(int.class)) {
      if (resolved.equals(0)) {
        return ArgumentOptional.of(null, FailReason.ARGUMENT_PARSED_NOT_THE_TYPE);
      }
    }
    if (argumentClass.isAssignableFrom(double.class)) {
      if (resolved.equals(0.0)) {
        return ArgumentOptional.of(null, FailReason.ARGUMENT_PARSED_NOT_THE_TYPE);
      }
    }
    if (resolved == null) {
      return ArgumentOptional.of(null, FailReason.ARGUMENT_PARSED_NOT_THE_TYPE);
    }

    return ArgumentOptional.of(resolved, FailReason.NO_FAIL_REASON);
  }

  /**
   * Gets a joined arguments into a message from the argument specified.
   *
   * @param from from which argument the joiner should start
   * @return joined string by argument with space as delimiter
   */
  public String getArgumentsJoined(int from) {
    StringBuilder builder = new StringBuilder();
    for (int i = from; i < args.size(); i++) {
      builder.append(args.get(i)).append(" ");
    }
    return builder.substring(0, builder.length() - 1);
  }

  /**
   * Returns the count of the specified arguments. This count will decrement whenever a argument was
   * got from any of the methods except {@link #getArgumentsJoined(int)}
   *
   * @return specified arguments count
   */
  public int size() {
    return args.size();
  }
}
