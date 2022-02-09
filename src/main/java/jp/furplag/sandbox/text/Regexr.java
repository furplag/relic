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

import java.io.Serializable;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jp.furplag.sandbox.stream.Streamr;
import jp.furplag.sandbox.trebuchet.Trebuchet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * optimized Unicode character(s) normalization for using under standard input .
 *
 * @author furplag
 *
 */
public interface Regexr extends Serializable, Comparable<Regexr> {

  /** remove control character . */
  static final Regexr removeCtrls = new Regexr.Origin("[\\p{Cc}&&[^\\s\\x{001C}-\\x{001F}]]", null, 0) {};

  /** remove invisible character . */
  static final Regexr removeEmpties = new Regexr.Origin("[\\x{2000}-\\x{200F}\\x{202A}-\\x{202F}\\x{2060}-\\x{206F}]", null, 1) {};

  /** replace white-spaces to space . */
  static final Regexr normalizeSpaces = new Regexr.Origin("[[\\p{javaWhitespace}\u00A0]&&[^\\n\u0020]]+", "\u0020", 10) {};

  /** replace a sequence of spaces with a single spaces . */
  static final Regexr spacesSinglize = new Regexr.Recursive("[\\p{javaWhitespace}&&[^\\n]]{2,}", "\u0020", 100) {};

  /** replace a sequence of line feeds with a single line feeds . */
  static final Regexr lineFeedsSinglize = new Regexr.Recursive("\\s+\\n|\\n\\s+", "\n", 1_000) {};

  /** remove leading and trailing space . */
  static final Regexr trim = new Regexr.Origin("^[\\p{javaWhitespace}]+|[\\p{javaWhitespace}]+$", null, 10_000) {};

  /**
   * modified normalization for CJK text .
   *
   * <p>
   * normalize the character member of Halfwidth and Fullwidth Forms uning {@link Form#NFKC} .
   * </p>
   * <ul>
   * <li>CJK half width character replace to full width mostly ( Hangul, Katakana, Hiragana ) .</li>
   * <li>CJK full width character replace to Latin or single byte character, if those are convertible .</li>
   * </ul>
   *
   */
  static final Regexr normalizeCjk = new Regexr.Origin("([\u3000-\u30FF\uFF00-\uFFEF&&[^\uFF5E\uFF04\uFFE0\uFFE1\uFFE5\uFFE6]]+)", "$1", 100_000) {

    private final UnaryOperator<String> _preNormalize = (t) -> Regexr.replaceAll(t
      , new Regexr.Origin("[\u2010-\u2012]", "\u002D", 1) {}
      , new Regexr.Origin("\u0020?[\u3099\u309B]", "\uFF9E", 2) {}
      , new Regexr.Origin("\u0020?[\u309A\u309C]", "\uFF9F", 3) {}
    );
    private final UnaryOperator<String> _normalize = (t) -> {
      final AtomicReference<String> result = new AtomicReference<>(t);
      pattern.matcher(result.get()).results().forEach((r) -> {
        final String _r = r.group();
        result.set(result.get().replace(_r, Normalizer.normalize(_r, Form.NFKC)));
      });

      return result.get();
    };
    private final UnaryOperator<String> _postNormalize = (t) -> Regexr.replaceAll(t
      , new Regexr.Origin("\u0020?([\u3099])", "\u309B", 1) {}
      , new Regexr.Origin("\u0020?([\u309A])", "\u309C", 2) {}
    );

    /** {@inheritDoc} */
    @Override
    public String replaceAll(final String text) {/* @formatter:off */
      return Objects.toString(text, "").isEmpty() ? text : Streamr.stream(text)
        .map(_preNormalize).map(_normalize).map(_postNormalize)
        .findFirst().orElse(null);
    /* @formatter:on */}
  };

  /**
   * returns result of evaluate that {@link #find(String)} .
   *
   * @param text to search in, may be null
   * @param regexrs {@link Regexr Regexr(s)}
   * @return {@code true} if, and only if, a subsequence of the input sequence matches this pattern
   */
  static boolean anyMatch(final String text, final Regexr... regexrs) {
    return Streamr.stream(regexrs).sorted().anyMatch((regexr) -> regexr.matches(text));
  }

  /**
   * shorthand for {@link String#codePoints()} .
   *
   * @param text the string(s), maybe null
   * @return an IntStream of Unicode code points from this strings
   */
  static IntStream codePoints(final String... text) {
    return Streamr.stream(text).collect(Collectors.joining()).codePoints();
  }

  /**
   * returns matched elements any of {@code regexrs} of the given string .
   *
   * @param text to search in, may be null
   * @param regexrs {@link Regexr Regexr(s)}
   * @return matched elements any of {@code regexrs} of the given string
   */
  static List<String> findAny(final String text, final Regexr... regexrs) {
    return Streamr.stream(regexrs).sorted().flatMap((regexr) -> regexr.find(text).stream()).collect(Collectors.toList());
  }

  /**
   * shorthand for {@code new String(((int[]) codePoints), 0, codePoints.length)} .
   *
   * @param codePoints Array that is the source of Unicode code points
   * @return the string represented by Unicode code points
   * @see {@link String#String(int[], int, int)}
   */
  static String newString(final int... codePoints) {
    final int[] temporal = Arrays.stream(Optional.ofNullable(codePoints).orElse(new int[] {})).filter(Character::isValidCodePoint).toArray();

    return temporal.length < 1 ? "" : new String(temporal, 0, temporal.length);
  }

  /**
   * shorthand for {@code regexrs.foreach(r->r.replaceAll(string))} .
   *
   * @param text to search in, may be null
   * @param regexrs {@link Regexr Regexr(s)}
   * @return the string constructed by replacing each matching subsequence by the replacement string
   */
  static String replaceAll(final String text, final Regexr... regexrs) {
    final String[] result = { text };
    Streamr.stream(regexrs).sorted().forEach((t) -> result[0] = t.replaceAll(result[0]));

    return result[0];
  }

  /**
   * a simply object which replacing text using RegEx .
   *
   * @author furplag
   *
   */
  @EqualsAndHashCode(doNotUseGetters = true)
  @ToString(includeFieldNames = true)
  abstract class Origin implements Regexr {

    /** regular expression compiled into a pattern . */
    @EqualsAndHashCode.Exclude
    @Getter
    protected final Pattern pattern;

    /** regular expression . */
    protected final String regex;

    /** the replacement string . */
    @Getter
    protected final String replacement;

    /** the order in replacing . */
    @EqualsAndHashCode.Exclude
    @Getter
    protected final int order;

    /**
     * @param regex the regular expression
     * @param replacement the replacement string, set empty string if this parameter is null
     */
    protected Origin(String regex, String replacement) {
      this(regex, replacement, 0);
    }

    /**
     * @param regex the regular expression, create do nothing operator if this parameter is empty
     * @param replacement the replacement string, set empty string if this parameter is null
     * @param order the order in replacing, set zero if this parameter is negative
     */
    public Origin(String regex, String replacement, int order) {
      this.pattern = Pattern.compile(Objects.toString(regex, "^\\x{0000}$"));
      this.regex = getPattern().pattern();
      this.replacement = Objects.toString(replacement, "");
      this.order = order < 0 ? 0 : order;
    }

    /** {@inheritDoc} */
    @Override
    public String getRegex() {
      return regex;
    }
  }

  /**
   * until you cry, I will not stop beating you .
   *
   * @author furplag
   *
   */
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true)
  abstract class Recursive extends Regexr.Origin {

    /**
     * @param regex the regular expression
     * @param replacement the replacement string, set empty string if this parameter is null
     */
    protected Recursive(String regex, String replacement) {
      super(regex, replacement);
    }

    /**
     * @param regex the regular expression
     * @param replacement the replacement string, set empty string if this parameter is null
     * @param order the order in replacing, meant zero if negative
     */
    protected Recursive(String regex, String replacement, int order) {
      super(regex, replacement, order);
    }

    /**
     * {@inheritDoc}
     * <p>recursive {@link Matcher#replaceAll(String)} .</p>
     * <p><b>Note: </b>be careful to infinity loop .</p>
     *
     */
    @Override
    public String replaceAll(final String text) {
      final String[] result = { text };
      while (!getReplacement().equals(result[0]) && matches(result[0])) {
        result[0] = super.replaceAll(result[0]);
      }

      return result[0];
    }
  }

  /** {@inheritDoc} */
  @Override
  default int compareTo(Regexr o) {
    return Trebuchet.Functions.orElse(getOrder(), o, (t, u) -> t.compareTo(o.getOrder()), () -> 1);
  }

  /**
   * returns matched elements of the given string .
   *
   * @param text to search in, may be null
   * @return matched elements of the given string
   */
  default List<String> find(final String text) {
    return getPattern().matcher(Objects.toString(text, "")).results().map(MatchResult::group).collect(Collectors.toList());
  }

  /**
   * returns result of evaluate that {@link Matcher#matches()} .
   *
   * @param text to be matched, may be null
   * @return {@code true} if, and only if, a subsequence of the input sequence matches this pattern
   *
   */
  default boolean matches(final String text) {
    return Trebuchet.Predicates.orNot(text, getPattern(), (t, u) -> u.matcher(t).find());
  }

  /**
   * replaces every subsequence of the input sequence that matches the pattern with the given replacement string .
   *
   * @param text to search and replace in, may be null
   * @return the string constructed by replacing each matching subsequence by the replacement string
   */
  default String replaceAll(final String text) {
    return Trebuchet.Functions.orElse(text, getPattern(), getReplacement(), (t, u, v) -> u.matcher(t).replaceAll(v), () -> text);
  }

  /**
   * returns the regular expression constructs of this instance .
   *
   * @return the regular expression constructs
   */
  Pattern getPattern();

  /**
   * returns the replacement string of this instance .
   *
   * @return the replacement string
   */
  String getReplacement();

  /**
   * returns the regular expression from which this pattern was compiled .
   *
   * @return the regular expression
   */
  default String getRegex() {
    return Trebuchet.Functions.orNot(getPattern(), Pattern::pattern);
  }

  /**
   * returns the sort order of this instance .
   *
   * @return the sort order
   */
  int getOrder();

  public static void main(String[] args) {
    System.out.println(removeCtrls.getPattern().matcher(null));
  }
}
