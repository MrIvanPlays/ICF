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

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public final class HelpPaginator {

  private final List<List<Entry<String, HelpEntry>>> pages;

  public HelpPaginator(List<Entry<String, HelpEntry>> allEntries) {
    List<List<Entry<String, HelpEntry>>> partitions = new ArrayList<>();
    for (int i = 0; i < allEntries.size(); i += 10) {
      partitions.add(allEntries.subList(i, Math.min(i + 10, allEntries.size())));
    }
    pages = partitions;
  }

  public List<Entry<String, HelpEntry>> getPage(int page) {
    return pages.get(page - 1);
  }

  public int pageCount() {
    return pages.size();
  }
}