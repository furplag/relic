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
package jp.furplag.sandbox.text;

import java.lang.Character.UnicodeBlock;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jp.furplag.sandbox.stream.Streamr;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * optimized Unicode character(s) normalization for using under standard input .
 *
 * @author furplag
 *
 */
public abstract class Commonizr {

  /** Hiragana convert to Katakana . */
  private static final Normalizr.Kanizr hiraganizr = new Normalizr.Kanizr('あ' - 'ア', UnicodeBlock.KATAKANA, 12448, 12535, 12536, 12537, 12538, 12539, 12540, 12543) {};

  /** Katakana convert to Hiragana . */
  private static final Normalizr.Kanizr katakanizr = new Normalizr.Kanizr(-hiraganizr.differenceOfCodepoints, UnicodeBlock.HIRAGANA, 12352, 12439, 12440, 12441, 12442, 12443, 12444, 12447) {};

  /** optimized Unicode character(s) normalization . */
  private static final Normalizr cjkNormalizr = new Normalizr('！' - '!') {

    /** exclude characters from translate. */
    private final Map<Integer, Integer> exclusives = Streamr.stream(new Integer[][]{/* @formatter:off */
      {0xFF65, 0x00B7}, {0xFFE0, 0x00A2}, {0xFFE1, 0x00A3}, {0xFFE2, 0x00AC}, {0xFFE3, 0x00AF}, {0xFFE4, 0x00A6}, {0xFFE5, 0x20A9}, {0xFFE6, 0x00A5}, {0xFFE8, 0x2502}
    /* @formatter:on */})
    .collect(Collectors.toUnmodifiableMap((k) -> k[1], (v) -> v[0], (a, b) -> b));

    /** {@inheritDoc} */
    @Override
    String normalize(String text) {
      return optimize(Regexr.normalizeCjk.replaceAll(text));
    }

    /** {@inheritDoc} */
    @Override
    int translate(int codePoint) {
      return exclusives.getOrDefault(codePoint, codePoint + (!UnicodeBlock.BASIC_LATIN.equals(UnicodeBlock.of(codePoint)) || Character.isWhitespace(codePoint) ? 0 : differenceOfCodepoints));
    }
  };

  /**
   * text normalization for using under standard input .
   *
   * @author furplag
   *
   */
  @AllArgsConstructor
  static abstract class Normalizr {

    /** difference of codepoint between two characters to convert . */
    final int differenceOfCodepoints;

    /**
     * convert between Katakana and Hiragana .
     *
     * @author furplag
     *
     */
    static abstract class Kanizr extends Normalizr {

      /** exclude characters from converting . */
      private final Set<Integer> exclusions;

      /** Unicode block to convert . */
      private final UnicodeBlock targetCodeBlock;

      Kanizr(int defferenceOfCodepoints, @NonNull UnicodeBlock targetCodeBlock, int... exclusions) {
        super(defferenceOfCodepoints);
        this.targetCodeBlock = targetCodeBlock;
        this.exclusions = Collections.unmodifiableSet(Arrays.stream(Optional.ofNullable(exclusions).orElse(new int[] {})).mapToObj(Integer::valueOf).collect(Collectors.toSet()));
      }

      /** {@inheritDoc} */
      @Override
      String normalize(final String text) {
        return Objects.isNull(text) ? text : normalizeCjk(text).codePoints()
          .mapToObj((i) -> Regexr.newString(translate(i)))
          .collect(Collectors.joining());
      }

      /** {@inheritDoc} */
      @Override
      int translate(final int codePoint) {
        return codePoint + (!exclusions.contains(codePoint) && targetCodeBlock.equals(UnicodeBlock.of(codePoint)) ? differenceOfCodepoints : 0);
      }
    }

    /**
     * returns normalized string for using under standard input text .
     *
     * @param text the string, maybe null
     * @return normalized text, return null if the text is null
     */
    abstract String normalize(final String text);

    /**
     * convert each number of characters .
     *
     * @param codePoint the number of character
     * @return converted codePoint using {@link #differenceOfCodepoints}, and something else
     */
    abstract int translate(final int codePoint);
  }

  /**
   * Katakana convert to Hiragana .
   *
   * @param text the string, maybe null
   * @return converted text, return null if the text is null
   */
  public static String hiraganize(final String text) {
    return Regexr.replaceAll(hiraganizr.normalize(normalizeCjk(text)), new Regexr.Origin("\\x{30F7}", "\u308F\u309B") {}, new Regexr.Origin("\\x{30F8}", "\u3090\u309B") {}, new Regexr.Origin("\\x{30F9}", "\u3091\u309B") {}, new Regexr.Origin("\\x{30FA}", "\u3092\u309B") {});
  }

  /**
   * Hiragana convert to Katakana .
   *
   * @param text the string, maybe null
   * @return converted text, return null if the text is null
   */
  public static String katakanize(final String text) {
    return katakanizr.normalize(text);
  }

  /**
   * returns denormalized string for using under standard input text .
   *
   * <ul>
   * <li>optimize text using {@link #optimize(String)} .</li>
   * <li>similar hyphens normalize to hyphen .</li>
   * <li>Latin or single byte character replace to CJK full width, if those are convertible .</li>
   * <li>Combining-Voiced-Soundmark replace to Full-Width-Voiced-Soundmark .</li>
   * <li>CJK half width character replace to full width mostly ( Hangul, Katakana, Hiragana ) .</li>
   * </ul>
   *
   * @param text the string, maybe null
   * @return denormalized text, return null if the text is null
   */
  public static String denormalizeCjk(final String text) {
    return Objects.isNull(text) ? text : normalizeCjk(text).codePoints().mapToObj((i) -> Regexr.newString(cjkNormalizr.translate(i))).collect(Collectors.joining()).replaceAll("\u0020", "\u3000");
  }

  /**
   * returns normalized string for using under standard input text .
   *
   * <ul>
   * <li>optimize text using {@link #optimize(String)} .</li>
   * <li>similar hyphens normalize to hyphen .</li>
   * <li>CJK half width character replace to full width mostly ( Hangul, Katakana, Hiragana ) .</li>
   * <li>CJK full width character replace to Latin or single byte character, if those are convertible .</li>
   * <li>Combining-Voiced-Soundmark replace to Full-Width-Voiced-Soundmark .</li>
   * </ul>
   *
   * @param text the string, maybe null
   * @return normalized text, return null if the text is null
   */
  public static String normalizeCjk(final String text) {
    return cjkNormalizr.normalize(optimize(text));
  }

  /**
   * optimized Unicode character(s) normalization for using under standard input .
   *
   * <ol>
   * <li>remove control character .</li>
   * <li>remove invisible character, like "Format characters" .</li>
   * <li>replace white-spaces to space .</li>
   * <li>replace a sequence of spaces with a single spaces .</li>
   * <li>replace a sequence of line feeds with a single line feeds .</li>
   * <li>remove leading and trailing space .</li>
   * </ol>
   *
   * @param text the string, maybe null
   * @return optimized text, return null if the text is null
   */
  public static String optimize(final String text) {
    return Regexr.replaceAll(text, Regexr.removeCtrls, Regexr.removeEmpties, Regexr.normalizeSpaces, Regexr.spacesSinglize, Regexr.lineFeedsSinglize, Regexr.trim);
  }

  /**
   * remove leading and trailing space .
   *
   * <ol>
   * <li>remove control character .</li>
   * <li>replace white-spaces to space .</li>
   * <li>remove leading and trailing space .</li>
   * </ol>
   *
   * @param text the string, maybe null
   * @return trimmed text, return null if the text is null
   */
  public static String trim(final String text) {
    return Regexr.replaceAll(text, Regexr.removeCtrls, Regexr.removeEmpties, new Regexr.Origin("^[\\p{javaWhitespace}\u00A0]+|[\\p{javaWhitespace}\u00A0]+$", null, 10) {}, Regexr.trim);
  }

  /**
   * do {@link #trim(String)}ming per reading line .
   *
   * @param text the string, maybe null
   * @return trimmed text, return null if the text is null
   */
  public static String trimMultiline(final String text) {
    return Regexr.replaceAll(trim(text), Regexr.lineFeedsSinglize, Regexr.trim);
  }

  /** the instance should NOT be constructed in standard programming. */
  private Commonizr() {}
}
