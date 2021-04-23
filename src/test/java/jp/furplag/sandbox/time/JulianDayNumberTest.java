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

class JulianDayNumberTest {

  @Test
  void test() {

    LongStream.rangeClosed(0, 100_000).forEach((l) -> {
      final Instant instant = Instant.parse("1996-01-23T12:34:56.789Z").plus(l, ChronoUnit.DAYS);

      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), JulianDayNumber.of(JulianDayNumber.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay)).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), JulianDayNumber.of(JulianDayNumber.ofEpochMilli(instant.toEpochMilli())).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), JulianDayNumber.of(JulianDayNumber.ofEpochSecond(instant.toEpochMilli() / 1000L)).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), JulianDayNumber.of(JulianDayNumber.ofInstant(instant)).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), JulianDayNumber.of(JulianDayNumber.ofJulian(Julian.ofInstant(instant))).toLocalDate(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.JULIAN_DAY), JulianDayNumber.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.JULIAN_DAY), JulianDayNumber.ofEpochMilli(instant.toEpochMilli()), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.JULIAN_DAY), JulianDayNumber.ofEpochSecond(instant.toEpochMilli() / 1000L), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.JULIAN_DAY), JulianDayNumber.ofInstant(instant), Objects.toString(l));

      assertEquals(LocalDateTime.ofInstant(instant.truncatedTo(ChronoUnit.DAYS), ZoneOffset.UTC), JulianDayNumber.of(JulianDayNumber.ofInstant(instant)).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofInstant(instant)).to(Millis).toInstant(), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofInstant(instant)).to(Julian).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), JulianDayNumber.of(JulianDayNumber.ofInstant(instant)).to(JulianDayNumber).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofInstant(instant)).to(ModifiedJulian).toInstant(), Objects.toString(l));
    });

    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofEpochDay(0L)).toInstant());
    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofEpochMilli(0L)).toInstant());
    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofEpochSecond(0L)).toInstant());

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofEpochDay(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofEpochMilli(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofEpochSecond(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofInstant(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), JulianDayNumber.of(JulianDayNumber.ofJulian(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
  }
}
