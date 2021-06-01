/**
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
package jp.furplag.sandbox.l10n;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * snippet of {@link Locale} and {@link TimeZone} .
 *
 * @author furplag
 *
 */
public final class Localizr {

  private Localizr() {; /* Localizer instances should NOT be constructed in standard programming . */}

  /** available Locales . */
  private static final Map<String, Locale> locales = Collections.unmodifiableMap(
    Arrays.stream(Locale.getAvailableLocales()).map((l) -> Map.entry(l.toString(), l)).flatMap((e) -> Stream.of(
      e
    , Map.entry(e.getKey().toLowerCase(), e.getValue())
    , Map.entry(Arrays.stream(e.getKey().split("_")).filter(StringUtils::isNotBlank).collect(Collectors.joining("_")), e.getValue())
    , Map.entry(Arrays.stream(e.getKey().split("_")).filter(StringUtils::isNotBlank).map(String::toLowerCase).collect(Collectors.joining("_")), e.getValue())
    , Map.entry(e.getValue().getDisplayName(Locale.ROOT), e.getValue())
  )).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new)));

  /** available TimeZones . */
  private static final Map<String, TimeZone> timeZones = Collections.unmodifiableMap(
    Stream.concat(Arrays.stream(TimeZone.getAvailableIDs()), ZoneId.getAvailableZoneIds().stream())
      .map((tz) -> Map.entry(tz, TimeZone.getTimeZone(tz)))
      .flatMap((e) -> Stream.of(e, Map.entry(e.getKey().toLowerCase(), e.getValue()), Map.entry(e.getValue().getDisplayName(Locale.ROOT), e.getValue())))
      .filter((e) -> Objects.nonNull(e.getValue()))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> next, HashMap::new))
  );

  /**
   * simple wrapper for {@link Locale} .
   *
   * @param locale locale String, e.g.) en_US, ja_JP_JP
   * @return {@link Locale}, or {@link Locale#getDefault()} if not match
   */
  public static Locale getLocale(String locale) {
    return locales.getOrDefault(Objects.requireNonNullElse(locale, Locale.getDefault().toString()), Locale.getDefault());
  }

  /**
   * simple wrapper for {@link TimeZone} .
   *
   * @param timeZone timeZone String, e.g.) UTC, PST, Asia/Tokyo
   * @return {@link TimeZone}, or {@link TimeZone#getDefault()} if not match
   */
  public static TimeZone getTimeZone(String timeZone) {
    return timeZones.getOrDefault(Objects.requireNonNullElse(timeZone, TimeZone.getDefault().toString()), TimeZone.getDefault());
  }
}
