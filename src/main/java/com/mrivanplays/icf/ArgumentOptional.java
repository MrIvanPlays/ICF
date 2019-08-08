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

import com.google.common.base.Preconditions;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a optional which contains things for our purpose and that's arguments.
 *
 * @param <T> argument type
 */
public final class ArgumentOptional<T> {

  /**
   * Creates a new argument optional. If the value given is null, the optional will be empty.
   *
   * @param value the value of which you want argument optional
   * @param failReason the fail reason of why this argument optional would fail
   * @param <T> argument type
   * @return argument optional if value not null, empty argument optional else
   */
  public static <T> ArgumentOptional<T> of(T value, FailReason failReason) {
    return value != null
        ? new ArgumentOptional<>(value, failReason)
        : new ArgumentOptional<>(null, failReason);
  }

  private final T value;
  private final FailReason failReason;

  private ArgumentOptional(T value, FailReason failReason) {
    this.value = value;
    this.failReason = failReason;
  }

  /**
   * Returns a <i>rest</i> argument action, which contains data about if the argument was null or
   * not. The method consumes the specified {@link Consumer} if a value is present, making the
   * <i>rest</i> action do nothing when method's invoked. If a value is not present, the <i>rest</i>
   * action comes to work.
   *
   * @param action executor of the argument
   * @return a <i>rest</i> argument action
   */
  public RestArgumentAction ifPresent(Consumer<T> action) {
    if (isPresent()) {
      action.accept(value);
      return new RestArgumentAction(false, failReason);
    } else {
      return new RestArgumentAction(true, failReason);
    }
  }

  /**
   * Maps the specified argument to a new value.
   *
   * @param mapper mapper for converting the current argument to another
   * @param <U> new argument type
   * @return argument optional with the new argument if present or a empty optional if the value was
   *     not present.
   */
  public <U> ArgumentOptional<U> map(Function<T, U> mapper) {
    Preconditions.checkNotNull(mapper, "mapper");
    if (isPresent()) {
      return ArgumentOptional.of(mapper.apply(value), failReason);
    } else {
      return ArgumentOptional.of(null, failReason);
    }
  }

  /**
   * Returns whenever the value is present.
   *
   * @return <code>true</code> if value present, <code>false</code> otherwise
   */
  public boolean isPresent() {
    return value != null;
  }

  /**
   * Gets the specified value if present. If the value is not present, the method will throw a
   * {@link NullPointerException}. It is required to use instead {@link #ifPresent(Consumer)} to
   * access the value, which also provides you handling when the value is not present.
   *
   * @return value if present
   * @throws NullPointerException if value not present
   */
  public T get() {
    Preconditions.checkNotNull(value, "Optional is empty");
    return value;
  }
}
