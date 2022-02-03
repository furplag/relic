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

/**
 * for text linting .
 *
 * @author furplag
 *
 */
public class RegexrStandard extends Regexr {

  /**
   *
   * @param regex the regular expression
   * @param replacement The replacement string, set empty string if this parameter is null
   */
  public RegexrStandard(String regex, String replacement) {
    super(regex, replacement, 0);
  }

  /**
   *
   * @param regex the regular expression
   * @param replacement The replacement string, set empty string if this parameter is null
   * @param order the order in replecing
   */
  public RegexrStandard(String regex, String replacement, int order) {
    super(regex, replacement, order);
  }
}
