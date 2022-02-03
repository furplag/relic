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
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jp.furplag.sandbox.text.regex.RegexrOrigin;
import jp.furplag.sandbox.text.regex.RegexrStandard;

/**
 * Kanizr flatten out Kana to Hiragana and Katakana .
 *
 * @author furplag
 *
 */
public final class Kanizr {

  /** difference of code point of the between Hiragana and Katakana. */
  private static final int differenceOfCodepoint;

  /** Katakana convert to Hiragana. */
  private static final Kanizr Hiraganizr;

  /** Hiragana convert to Katakana. */
  private static final Kanizr Katakanizr;
  static {
    differenceOfCodepoint = 'あ' - 'ア';
    Hiraganizr = new Kanizr(UnicodeBlock.KATAKANA, differenceOfCodepoint, 12448, 12535, 12536, 12537, 12538, 12539, 12540, 12543);
    Katakanizr = new Kanizr(UnicodeBlock.HIRAGANA, -differenceOfCodepoint, 12352, 12439, 12440, 12441, 12442, 12443, 12444, 12447);
  }

  /** Unicode code block to convert. */
  private final UnicodeBlock targetCodeBlock;

  /** the amount of add to Unicode code point of the target character. */
  private final int gap;

  /** exclude characters from translate. */
  private final Set<Integer> exclusions;

  /**
   *
   * @param targetCodeBlock Unicode code block to convert
   * @param gap the amount of add to Unicode code point of the target character
   * @param exclusions exclude characters from translate
   */
  private Kanizr(UnicodeBlock targetCodeBlock, int gap, int... exclusions) {
    this.targetCodeBlock = Objects.requireNonNull(targetCodeBlock);
    this.gap = gap;
    this.exclusions = Collections.unmodifiableSet(Arrays.stream(Optional.ofNullable(exclusions).orElse(new int[] {})).mapToObj(Integer::valueOf).collect(Collectors.toSet()));
  }

  /**
   * Katakana convert to Hiragana .
   *
   * @param string the String, maybe null
   * @return Hiraganized text
   */
  public static String hiraganize(final String string) {
    return RegexrOrigin.isEmpty(string) ? string
        : RegexrOrigin.replaceAll(Hiraganizr.kanize(string), new RegexrStandard("\\x{30F7}", "\u308F\u309B"), new RegexrStandard("\\x{30F8}", "\u3090\u309B"), new RegexrStandard("\\x{30F9}", "\u3091\u309B"), new RegexrStandard("\\x{30FA}", "\u3092\u309B"));
  }

  /**
   * Hiragana convert to Katakana .
   *
   * @param string the String, maybe null
   * @return Katakanized text
   */
  public static String katakanize(final String string) {
    return Katakanizr.kanize(string);
  }

  /**
   * flatten out Kana to Hiragana and Katakana .
   *
   * @param string the String, maybe null
   * @return converted text
   */
  private String kanize(String string) {
    // @formatter:off
      return RegexrOrigin.isEmpty(string) ? string :
        Optional.ofNullable(CjkNormalizr.normalize(string)).orElse("")
          .codePoints()
          .map(this::translate)
          .mapToObj(RegexrOrigin::newString)
          .collect(Collectors.joining())
        ;
      // @formatter:on
  }

  /**
   * convert Kana .
   *
   * @param codePoint Unicode code point
   * @return converted Kana if the character is the member of {@code targetCodeBlock}
   */
  private int translate(final int codePoint) {
    return codePoint + (!exclusions.contains(codePoint) && targetCodeBlock.equals(UnicodeBlock.of(codePoint)) ? gap : 0);
  }
}
