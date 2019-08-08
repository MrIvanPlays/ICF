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

public class ArgumentOptional<T> {

  public static <T> ArgumentOptional<T> of(T value) {
    return value != null ? new ArgumentOptional<>(value) : new ArgumentOptional<>(null);
  }

  private final T value;

  private ArgumentOptional(T value) {
    this.value = value;
  }

  public RestArgumentAction ifPresent(Consumer<T> action) {
    if (isPresent()) {
      action.accept(value);
      return new RestArgumentAction(false);
    } else {
      return new RestArgumentAction(true);
    }
  }

  public boolean isPresent() {
    return value != null;
  }

  public T get() {
    Preconditions.checkNotNull(value, "Optional is empty");
    return value;
  }
}
