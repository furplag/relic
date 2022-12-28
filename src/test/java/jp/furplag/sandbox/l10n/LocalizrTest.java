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
package jp.furplag.sandbox.l10n;

import static org.junit.jupiter.api.Assertions.*;

import java.time.ZoneId;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class LocalizrTest {

  @Test
  void testLocales() {
    /* @formatter:off */
    assertFalse(
        Arrays.stream(Optional.ofNullable(Locale.getAvailableLocales()).orElse(new Locale[] {}))
            .flatMap(
                (l) ->
                    Stream.of(
                        l.toString(),
                        l.toString().toLowerCase(),
                        Arrays.stream(l.toString().split("_"))
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining("_")),
                        Arrays.stream(l.toString().split("_"))
                            .filter(StringUtils::isNotBlank)
                            .map(String::toLowerCase)
                            .collect(Collectors.joining("_")),
                        l.getDisplayName(Locale.ROOT)))
            .map(Localizr::getLocale)
            .anyMatch(Objects::isNull));

    assertEquals(Locale.getDefault(), Localizr.getLocale(null));
    assertEquals(Locale.getDefault(), Localizr.getLocale("Not exists"));
    assertEquals(Locale.getDefault(), Localizr.getLocale(Locale.getDefault().toString()));
    assertEquals(
        Locale.getDefault(), Localizr.getLocale(Locale.getDefault().toString().toLowerCase()));

    assertEquals(Locale.ROOT, Localizr.getLocale(""));
    /* @formatter:on */ }

  @Test
  void testTimeZones() {
    /* @formatter:off */
    assertFalse(
        Stream.concat(
                Arrays.stream(TimeZone.getAvailableIDs()), ZoneId.getAvailableZoneIds().stream())
            .flatMap(
                (tz) ->
                    Stream.of(
                        tz, tz.toLowerCase(), TimeZone.getTimeZone(tz).getDisplayName(Locale.ROOT)))
            .map(Localizr::getTimeZone)
            .anyMatch(Objects::isNull));

    assertEquals(TimeZone.getDefault(), Localizr.getTimeZone(null));
    assertEquals(TimeZone.getDefault(), Localizr.getTimeZone(""));
    assertEquals(TimeZone.getDefault(), Localizr.getTimeZone("Not exists"));
    assertEquals(TimeZone.getDefault(), Localizr.getTimeZone(ZoneId.systemDefault().toString()));
    assertEquals(TimeZone.getDefault(), Localizr.getTimeZone(TimeZone.getDefault().toString()));
    assertEquals(
        TimeZone.getDefault(),
        Localizr.getTimeZone(ZoneId.systemDefault().toString().toLowerCase()));
    assertEquals(
        TimeZone.getDefault(),
        Localizr.getTimeZone(TimeZone.getDefault().toString().toLowerCase()));
    /* @formatter:on */ }
}
