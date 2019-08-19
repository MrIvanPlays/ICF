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
import java.util.Optional;
import java.util.function.Function;

/** Represents a class, containing the actual argument resolving of a command. */
public final class CommandArguments {

  private final List<String> args;

  public CommandArguments(String[] args) {
    this(new ArrayList<>(Arrays.asList(args)));
  }

  private CommandArguments(List<String> args) {
    this.args = args;
  }

  /**
   * Gets the next argument while decrementing the {@link #size()}. This method is unsafe as the
   * method may return null if there are no arguments and the safe alternatives of this method are
   * {@link #next()} or {@link #next(Function)}. Be careful when using the methods with <code>next
   * </code> in their name!
   *
   * @return a string argument or null
   */
  public String nextUnsafe() {
    return getArgUnsafe(0);
  }

  /**
   * Gets the next argument while decrementing the {@link #size()}. Be careful when using the
   * methods with <code>next</code> in their name!
   *
   * @return a optional with string argument if present, a empty optional if not present
   */
  public Optional<String> next() {
    return Optional.ofNullable(nextUnsafe());
  }

  /**
   * Gets the specified argument while decrementing the {@link #size()}. This method is unsafe as
   * the method may return null if there are no arguments and the safe alternative of this method is
   * {@link #getArg(int)}. Be very careful how you use this.
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
   * Gets the specified argument while decrementing the {@link #size()}. Be very careful how you use
   * this.
   *
   * @param arg the argument yiu want to get
   * @return a optional with string argument if present, a empty optional if not present
   */
  public Optional<String> getArg(int arg) {
    return Optional.ofNullable(getArgUnsafe(arg));
  }

  /**
   * Resolves the next argument to the specified resolver. The specified method decrements {@link
   * #size()} and if you run that method like that:
   *
   * <blockquote>
   *
   * <pre>
   *   public void execute(CommandSender cs, String label, CommandArguments args) {
   *     args.next(ArgumentResolvers.STRING).ifPresent(string (lambda) {
   *       // handling
   *     }).orElse(failReason (lambda) {
   *       // handling
   *     });
   *     args.next(ArgumentResolvers.STRING).ifPresent(string (lambda) {
   *       // handling
   *     }).orElse(failReason (lambda) {
   *       // handling
   *     });
   *   }
   * </pre>
   *
   * </blockquote>
   *
   * The 2nd get will get the next argument after the 1st call so it won't be equal to the first
   * one. That's why 2nd or more arguments should be in the call before to have access to all of the
   * arguments you need. Be careful when using the methods with <code>next</code> in their name!
   *
   * @param resolver the resolver of the argument you want to resolve.
   * @param <T> the type of the argument
   * @return empty {@link ArgumentOptional} if argument not parsed, or the argument parsed is not
   *     the type, or the type parsed is null.
   */
  public <T> ArgumentOptional<T> next(ArgumentResolver<T> resolver) {
    if (args.size() == 0) {
      return ArgumentOptional.of(null, FailReason.ARGUMENT_NOT_TYPED);
    }
    try {
      T resolved = resolver.resolve(nextUnsafe());
      if (resolved == null) {
        return ArgumentOptional.of(null, FailReason.ARGUMENT_PARSED_NULL);
      }
      return ArgumentOptional.of(resolved, FailReason.NO_FAIL_REASON);
    } catch (Throwable error) {
      return ArgumentOptional.of(null, FailReason.ARGUMENT_PARSED_NOT_TYPE);
    }
  }

  public ArgumentOptional<Integer> nextInt() {
    return next(ArgumentResolvers.INTEGER);
  }

  public ArgumentOptional<String> nextString() {
    return next(ArgumentResolvers.STRING);
  }

  public ArgumentOptional<Double> nextDouble() {
    return next(ArgumentResolvers.DOUBLE);
  }

  /**
   * Resolves the next argument to the specified resolver. The specified method decrements {@link
   * #size()} and if you run that method like that:
   *
   * @param resolver the resolver of the argument you want to resolve.
   * @param <T> the type of the argument
   * @return empty {@link ArgumentOptional} if argument not parsed, or the argument parsed is not
   *     the type, or the type parsed is null.
   * @see #next(ArgumentResolver)
   */
  public <T> ArgumentOptional<T> next(Function<String, T> resolver) {
    return next((ArgumentResolver<T>) resolver::apply);
  }

  /**
   * Joins the specified arguments with space as a delimiter.
   *
   * @param from from which argument the joiner should start
   * @return joined string from arguments with space as delimiter
   */
  public String joinArgumentsSpace(int from) {
    return joinArguments(from, ' ');
  }

  /**
   * Joins the specified arguments with the character specified.
   *
   * @param from from which argument the joiner should start
   * @param separator the separator used to join the arguments
   * @return joined string from arguments with the separator as delimiter
   */
  public String joinArguments(int from, char separator) {
    StringBuilder builder = new StringBuilder();
    for (int i = from; i < args.size(); i++) {
      builder.append(args.get(i)).append(separator);
    }
    return builder.substring(0, builder.length() - 1);
  }

  /**
   * Returns the count of the specified arguments. This count will decrement whenever a argument was
   * got from any of the methods except {@link #joinArguments(int, char)}
   *
   * @return specified arguments count
   */
  public int size() {
    return args.size();
  }

  /**
   * Creates a new copy of this command arguments.
   *
   * @return instance copy
   */
  public CommandArguments copy() {
    return new CommandArguments(args);
  }
}
