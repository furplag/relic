/*
 * Copyright (C) 2018+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.furplag.sandbox.text.optimize;

import jp.furplag.sandbox.text.regex.Regexr;
import jp.furplag.sandbox.text.regex.RegexrOrigin;

/**
 * remove Control Character and remove leading and trailing space.
 *
 * @author furplag
 *
 */
public final class Trimr {

  private static final Regexr[] regexrs = {Regexr.CtrlRemovr, Regexr.Trimr};

  /**
   * Trimr instances should NOT be constructed in standard programming.
   */
  private Trimr() {}

  /**
   * detects whether the specified string is trimmed.
   *
   * @param string the string, maybe null
   * @return true if the specified string is trimmed
   */
  public static boolean isTrimmed(final String string) {
    return RegexrOrigin.isEmpty(string) || !RegexrOrigin.anyMatch(string, regexrs);
  }

  /**
   * remove Control Character and remove leading and trailing space.
   *
   * @param string the string, maybe null
   * @return trimmed text
   */
  public static String trim(final String string) {
    return RegexrOrigin.replaceAll(string, regexrs);
  }
}
