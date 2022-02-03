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
package jp.furplag.sandbox.text.regex;

import java.io.Serializable;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * for text linting .
 *
 * @author furplag
 *
 */
public abstract class Regexr implements RegexrOrigin, Serializable, Comparable<RegexrOrigin> {

  /** remove Control Character . */
  public static final Regexr CtrlRemovr;

  /** replace whitespaces to space . */
  public static final Regexr SpaceNormalizr;

  /** replace a sequence of spaces with a single spaces . */
  public static final Regexr SpaceLintr;

  /** remove empty rows . */
  public static final Regexr LinefeedLintr;

  /** remove leading and trailing space . */
  public static final Regexr Trimr;

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
  public static final Regexr CjkNormalizr;

  static {
    CtrlRemovr = new RegexrStandard("[\\p{Cc}&&[^\\s\\x{001C}-\\x{001F}]]", "", 0);
    SpaceNormalizr = new RegexrStandard("([[\\p{javaWhitespace}\u00A0]&&[^\\n\u0020]]+)", "\u0020", 10);
    SpaceLintr = new RegexrRecursive("[\\p{javaWhitespace}&&[^\\n]]{2,}", "\u0020", 100);
    LinefeedLintr = new RegexrRecursive("\\s+\\n|\\n\\s+", "\n", 1_000);
    Trimr = new RegexrStandard("^[\\p{javaWhitespace}]+|[\\p{javaWhitespace}]+$", "", 10_000);
    final UnaryOperator<String> preNormalize = (t) -> RegexrOrigin.replaceAll(t
        , new RegexrStandard("[\u2010-\u2012]", "\u002D", 1)
        , new RegexrStandard("\u0020?[\u3099\u309B]", "\uFF9E", 2)
        , new RegexrStandard("\u0020?[\u309A\u309C]", "\uFF9F", 3)
      );
    final UnaryOperator<String> postNormalize = (t) -> RegexrOrigin.replaceAll(t
        , new RegexrStandard("\u0020?([\u3099])", "\u309B", 1)
        , new RegexrStandard("\u0020?([\u309A])", "\u309C", 2)
      );
    CjkNormalizr = new Regexr("([\u3000-\u30FF\uFF00-\uFFEF&&[^\uFF5E\uFF04\uFFE0\uFFE1\uFFE5\uFFE6]]+)", "$1", 100_000) {

      @Override
      public String replaceAll(String string) {/* @formatter:off */
        return RegexrOrigin.isEmpty(string) ? string : Stream.of(string)
          .map(preNormalize)
          .map((t) -> {
            final String[] _result = { t };
            pattern.matcher(_result[0]).results().forEach((r) -> {
              final String _r = r.group();
              _result[0] = _result[0].replace(_r, Normalizer.normalize(_r, Form.NFKC));
            });
            return _result[0];
          }).map(postNormalize).findFirst().orElse(null);
        // @formatter:on
      }
    };
  }

  /** regular expression compiled into a pattern . */
  protected final Pattern pattern;

  /** the replacement string . */
  protected final String replacement;

  /** the order in replecing . */
  protected final int order;

  /**
   * shorthand for {@code Pattern.compile(regex).matcher(text).replaceAll(replacement)} .
   *
   * @param regex the regular expression
   * @param replacement The replacement string, set empty string if this parameter is null
   * @param order the order in replecing
   */
  protected Regexr(String regex, String replacement, int order) {
    this.pattern = Pattern.compile(Objects.toString(regex, "^\\x{0000}$"));
    this.replacement = Objects.toString(replacement, "");
    this.order = order < 0 ? 0 : order;
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(RegexrOrigin o) {
    return o == null ? 1 : Integer.valueOf(this.order()).compareTo(o.order());
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    return this == obj || (obj instanceof Regexr && this.toString().equals(((Regexr) obj).toString()));
  }

  /** {@inheritDoc} */
  @Override
  public List<String> find(String string) {
    List<String> result = new ArrayList<>();
    if (!RegexrOrigin.isEmpty(string)) {
      Matcher matcher = pattern.matcher(string);
      while (matcher.find()) {
        result.add(matcher.group());
      }
    }

    return result;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pattern == null) ? 0 : pattern.toString().hashCode());
    result = prime * result + ((replacement == null) ? 0 : replacement.hashCode());

    return result;
  }

  /** {@inheritDoc} */
  @Override
  public boolean matches(String string) {
    return !RegexrOrigin.isEmpty(string) && pattern.matcher(string).find();
  }

  /** {@inheritDoc} */
  @Override
  public final int order() {
    return order;
  }

  /** {@inheritDoc} */
  @Override
  public Pattern pattern() {
    return pattern;
  }

  /** {@inheritDoc} */
  @Override
  public String replaceAll(String string) {
    return RegexrOrigin.isEmpty(string) ? string : pattern.matcher(string).replaceAll(replacement);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return new StringJoiner(", ", "{", "}").add("pattern: " + Objects.toString(pattern)).add("replacement: " + Objects.toString(replacement)).toString();
  }
}
