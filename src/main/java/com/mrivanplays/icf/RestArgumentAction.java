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

/**
 * Represents a <i>rest</i> action, which comes role when the argument you want to get is null. This
 * provides a {@link #orElse(Runnable)} method, which should be used only when the argument you want
 * is not present.
 */
public final class RestArgumentAction {

  private final boolean valueNull;

  public RestArgumentAction(boolean valueNull) {
    this.valueNull = valueNull;
  }

  /**
   * Returns whenever the value was present.
   *
   * @return <code>true</code> if present, <code>false</code> otherwise
   */
  public boolean wasValuePresent() {
    return !valueNull;
  }

  /**
   * The specified {@link Runnable} gets invoked when the value wasn't present.
   *
   * @param runnable the runnable to run when the value isn't present.
   */
  public void orElse(Runnable runnable) {
    if (valueNull) {
      runnable.run();
    }
  }
}
