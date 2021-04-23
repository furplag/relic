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
package jp.furplag.sandbox.time;

import static jp.furplag.sandbox.time.Deamtiet.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.JulianFields;
import java.util.Objects;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;

class ModifiedJulianTest {

  @Test
  void test() {

    LongStream.rangeClosed(0, 100_000).forEach((l) -> {
      final Instant instant = Instant.parse("1996-01-23T12:34:56.789Z").plus(l, ChronoUnit.DAYS);

      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), ModifiedJulian.of(ModifiedJulian.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay)).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), ModifiedJulian.of(ModifiedJulian.ofEpochMilli(instant.toEpochMilli())).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant.truncatedTo(ChronoUnit.SECONDS), ZoneOffset.UTC), ModifiedJulian.of(ModifiedJulian.ofEpochSecond(instant.toEpochMilli() / 1000L)).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), ModifiedJulian.of(ModifiedJulian.ofInstant(instant)).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), ModifiedJulian.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay).longValue(), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), ModifiedJulian.ofEpochMilli(instant.toEpochMilli()).longValue(), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), ModifiedJulian.ofEpochSecond(instant.toEpochMilli() / 1000L).longValue(), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), ModifiedJulian.ofInstant(instant).longValue(), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), ModifiedJulian.ofJulian(Julian.ofInstant(instant)).longValue(), Objects.toString(l));

      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofInstant(instant)).to(Millis).toInstant().truncatedTo(ChronoUnit.SECONDS), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofInstant(instant)).to(Julian).toInstant().truncatedTo(ChronoUnit.SECONDS), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), ModifiedJulian.of(ModifiedJulian.ofInstant(instant)).to(JulianDayNumber).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofInstant(instant)).to(ModifiedJulian).toInstant().truncatedTo(ChronoUnit.SECONDS), Objects.toString(l));
    });

    assertEquals(Instant.EPOCH, ModifiedJulian.of(ModifiedJulian.ofEpochDay(0L)).toInstant());
    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofEpochMilli(0L)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofEpochSecond(0L)).toInstant().truncatedTo(ChronoUnit.SECONDS));

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), ModifiedJulian.of(ModifiedJulian.ofEpochDay(null)).toInstant());
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofEpochMilli(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofEpochSecond(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofInstant(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(ModifiedJulian.ofJulian(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
  }
}
