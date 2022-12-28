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
package jp.furplag.sandbox.time;

import static jp.furplag.sandbox.time.Deamtiet.julian;
import static jp.furplag.sandbox.time.Deamtiet.julianDayNumber;
import static jp.furplag.sandbox.time.Deamtiet.millis;
import static jp.furplag.sandbox.time.Deamtiet.modifiedJulian;
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

      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), modifiedJulian.of(modifiedJulian.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay)).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), modifiedJulian.of(modifiedJulian.ofEpochMilli(instant.toEpochMilli())).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant.truncatedTo(ChronoUnit.SECONDS), ZoneOffset.UTC), modifiedJulian.of(modifiedJulian.ofEpochSecond(instant.toEpochMilli() / 1000L)).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), modifiedJulian.of(modifiedJulian.ofInstant(instant)).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), modifiedJulian.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay).longValue(), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), modifiedJulian.ofEpochMilli(instant.toEpochMilli()).longValue(), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), modifiedJulian.ofEpochSecond(instant.toEpochMilli() / 1000L).longValue(), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), modifiedJulian.ofInstant(instant).longValue(), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.MODIFIED_JULIAN_DAY), modifiedJulian.ofJulian(julian.ofInstant(instant)).longValue(), Objects.toString(l));

      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofInstant(instant)).to(millis).toInstant().truncatedTo(ChronoUnit.SECONDS), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofInstant(instant)).to(julian).toInstant().truncatedTo(ChronoUnit.SECONDS), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), modifiedJulian.of(modifiedJulian.ofInstant(instant)).to(julianDayNumber).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofInstant(instant)).to(modifiedJulian).toInstant().truncatedTo(ChronoUnit.SECONDS), Objects.toString(l));
    });

    assertEquals(Instant.EPOCH, modifiedJulian.of(modifiedJulian.ofEpochDay(0L)).toInstant());
    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofEpochMilli(0L)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofEpochSecond(0L)).toInstant().truncatedTo(ChronoUnit.SECONDS));

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), modifiedJulian.of(modifiedJulian.ofEpochDay(null)).toInstant());
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofEpochMilli(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofEpochSecond(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofInstant(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(modifiedJulian.ofJulian(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), modifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
  }
}
