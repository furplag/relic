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
package jp.furplag.sandbox.text.normalize;

import java.lang.Character.UnicodeBlock;
import java.util.Map;
import java.util.stream.Collectors;
import jp.furplag.sandbox.stream.Streamr;
import jp.furplag.sandbox.text.optimize.Optimizr;
import jp.furplag.sandbox.text.regex.Regexr;
import jp.furplag.sandbox.text.regex.RegexrOrigin;

/**
 * normalize the string for using under standard input text .
 *
 * <ul>
 * <li>optimize text using {@link Optimizr#optimize(String)}.</li>
 * <li>similar hyphens normalize to hyphen.</li>
 * <li>hyphen normalize.</li>
 * <li>CJK half width character replace to full width mostly ( Hangul, Katakana, Hiragana ) .</li>
 * <li>CJK full width character replace to Latin or single byte character, if those are convertible.</li>
 * <li>Combining-Voiced-Soundmark replace to Full-Width-Voiced-Soundmark.</li>
 * </ul>
 *
 * @author furplag
 *
 */
public final class CjkNormalizr {

  /** the amount of add to Unicode code point of the target character. */
  private static final int differenceOfCodepoint = "！".codePointAt(0) - "!".codePointAt(0);

  /** exclude characters from translate. *//* @formatter:off */
  private static final Map<Integer, Integer> exclusives =
    Streamr.stream(new Integer[][]{
      {0xFF65, 0x00B7}, {0xFFE0, 0x00A2}, {0xFFE1, 0x00A3}, {0xFFE2, 0x00AC}, {0xFFE3, 0x00AF}, {0xFFE4, 0x00A6}, {0xFFE5, 0x20A9}, {0xFFE6, 0x00A5}, {0xFFE8, 0x2502}
    })
    .collect(Collectors.toUnmodifiableMap((k) -> k[1], (v) -> v[0], (a, b) -> b));
  /* @formatter:on */

  /**
   * returns denormalized string for using under standard input text .
   *
   * <ul>
   * <li>optimize text using {@link Optimizr#optimize(String)}.</li>
   * <li>similar hyphens normalize to hyphen.</li>
   * <li>hyphen normalize.</li>
   * <li>Latin or single byte character character replace to CJK full width, if those are convertible.</li>
   * <li>Combining-Voiced-Soundmark replace to Full-Width-Voiced-Soundmark.</li>
   * <li>CJK half width character replace to full width mostly ( Hangul, Katakana, Hiragana ) .</li>
   * </ul>
   *
   * @param string the string, maybe null
   * @return optimized text
   */
  public static String denormalize(final String string) {
    return RegexrOrigin.isEmpty(string) ? string : normalize(string).codePoints().map(CjkNormalizr::translate).mapToObj(RegexrOrigin::newString).collect(Collectors.joining()).replaceAll("\u0020", "\u3000");
  }

  /**
   * detects whether the specified string is normalized.
   *
   * @param string the string, maybe null
   * @return true if the specified string is normalized
   */
  public static boolean isNormalized(final String string) {
    return RegexrOrigin.isEmpty(string) || (Optimizr.isOptimized(string) && string.equals(normalize(string)));
  }

  /**
   * returns normalized string for using under standard input text .
   *
   * <ul>
   * <li>optimize text using {@link Optimizr#optimize(String)}.</li>
   * <li>similar hyphens normalize to hyphen.</li>
   * <li>hyphen normalize.</li>
   * <li>CJK half width character replace to full width mostly ( Hangul, Katakana, Hiragana ) .</li>
   * <li>CJK full width character replace to Latin or single byte character, if those are convertible.</li>
   * <li>Combining-Voiced-Soundmark replace to Full-Width-Voiced-Soundmark.</li>
   * </ul>
   *
   * @param string the string, maybe null
   * @return optimized text
   */
  public static String normalize(final String string) {
    return RegexrOrigin.isEmpty(string) ? string : Optimizr.optimize(RegexrOrigin.replaceAll(string, Regexr.CjkNormalizr));
  }

  /**
   * convert to full width character if the character is the member of {@link UnicodeBlock.BASIC_LATIN} .
   *
   * @param codePoint Unicode code point
   * @return text denormalize if the character is the member of {@link UnicodeBlock.BASIC_LATIN}
   */
  private static int translate(final int codePoint) {
    return exclusives.getOrDefault(codePoint, codePoint + (!UnicodeBlock.BASIC_LATIN.equals(UnicodeBlock.of(codePoint)) || Character.isWhitespace(codePoint) ? 0 : differenceOfCodepoint));
  }


  /** CjkNormalizr instances should NOT be constructed in standard programming. */
  private CjkNormalizr() {}
}
