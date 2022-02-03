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
 * optimize the string for using under standard input text .
 *
 * <ul>
 * <li>remove Control Character and remove leading and trailing space.</li>
 * <li>replace a sequence of spaces with a single space.</li>
 * <li>replace successive newlines with one newline.</li>
 * <li>remove empty rows.</li>
 * </ul>
 *
 * @author furplag
 *
 */
public final class Optimizr {

  /** {@link Regexr}. */
  private static final Regexr[] regexrs = {Regexr.CtrlRemovr, Regexr.SpaceNormalizr, Regexr.SpaceLintr, Regexr.LinefeedLintr, Regexr.Trimr};

  /**
   * Optimizr instances should NOT be constructed in standard programming.
   */
  private Optimizr() {}

  /**
   * detects whether the specified string is optimized.
   *
   * @param string the string, maybe null
   * @return true if the specified string is optimized
   */
  public static boolean isOptimized(final String string) {
    Regexr[] regexrs = {Regexr.CtrlRemovr, Regexr.SpaceLintr, Regexr.LinefeedLintr, Regexr.Trimr};

    return RegexrOrigin.isEmpty(string) || !RegexrOrigin.anyMatch(string, regexrs);
  }

  /**
   * returns optimized string for using under standard input text .
   *
   * <ul>
   * <li>remove Control Character and remove leading and trailing space.</li>
   * <li>replace a sequence of spaces with a single space.</li>
   * <li>replace successive newlines with one newline.</li>
   * <li>remove empty rows.</li>
   * </ul>
   *
   * @param string the string, maybe null
   * @return optimized text
   */
  public static String optimize(final String string) {
    return RegexrOrigin.replaceAll(string, regexrs);
  }
}
