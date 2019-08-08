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

import com.mrivanplays.icf.ICFCommand;
import com.mrivanplays.icf.helpapi.CommandDescription;
import com.mrivanplays.icf.helpapi.CommandSyntax;
import java.lang.annotation.Annotation;
import java.util.Optional;

public class AnnotationFinder {

  public static <T extends ICFCommand> Optional<String> findDescription(T command) {
    Annotation[] annotations = command.getClass().getAnnotations();
    if (annotations == null || annotations.length == 0) {
      return Optional.empty();
    }
    for (Annotation annotation : annotations) {
      if (annotation instanceof CommandDescription) {
        CommandDescription description = (CommandDescription) annotation;
        return Optional.of(description.value());
      }
    }
    return Optional.empty();
  }

  public static <T extends ICFCommand> Optional<String> findSyntax(T command) {
    Annotation[] annotations = command.getClass().getAnnotations();
    if (annotations == null || annotations.length == 0) {
      return Optional.empty();
    }
    for (Annotation annotation : annotations) {
      if (annotation instanceof CommandSyntax) {
        CommandSyntax syntax = (CommandSyntax) annotation;
        return Optional.of(syntax.value());
      }
    }
    return Optional.empty();
  }
}
