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

import java.util.regex.Matcher;

/**
 * recursive text replacing .
 *
 * @author furplag
 *
 */
public class RegexrRecursive extends Regexr {

  /**
   *
   * @param regex the regular expression
   * @param replacement The replacement string, set empty string if this parameter is null
   */
  public RegexrRecursive(String regex, String replacement) {
    super(regex, replacement, 0);
  }

  /**
   *
   * @param regex the regular expression
   * @param replacement The replacement string, set empty string if this parameter is null
   * @param order the order in replecing
   */
  public RegexrRecursive(String regex, String replacement, int order) {
    super(regex, replacement, order);
  }

  /**
   * {@inheritDoc}
   * <p>recursive {@link Matcher#replaceAll(String)} .</p>
   * <p><b>Note: </b>be careful to infinity loop .</p>
   *
   */
  @Override
  public String replaceAll(final String string) {
    if (RegexrOrigin.isEmpty(string)) return string;
    String result = string;
    Matcher matcher = pattern.matcher(result);
    while (matcher.find()) {
      result = matcher.replaceAll(replacement);
      matcher.reset(result);
    }

    return result;
  }
}
