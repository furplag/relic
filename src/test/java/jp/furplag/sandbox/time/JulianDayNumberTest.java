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

class JulianDayNumberTest {

  @Test
  void test() {

    LongStream.rangeClosed(0, 100_000).forEach((l) -> {
      final Instant instant = Instant.parse("1996-01-23T12:34:56.789Z").plus(l, ChronoUnit.DAYS);

      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), julianDayNumber.of(julianDayNumber.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay)).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), julianDayNumber.of(julianDayNumber.ofEpochMilli(instant.toEpochMilli())).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), julianDayNumber.of(julianDayNumber.ofEpochSecond(instant.toEpochMilli() / 1000L)).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), julianDayNumber.of(julianDayNumber.ofInstant(instant)).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), julianDayNumber.of(julianDayNumber.ofJulian(julian.ofInstant(instant))).toLocalDate(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.JULIAN_DAY), julianDayNumber.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.JULIAN_DAY), julianDayNumber.ofEpochMilli(instant.toEpochMilli()), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.JULIAN_DAY), julianDayNumber.ofEpochSecond(instant.toEpochMilli() / 1000L), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC).getLong(JulianFields.JULIAN_DAY), julianDayNumber.ofInstant(instant), Objects.toString(l));

      assertEquals(LocalDateTime.ofInstant(instant.truncatedTo(ChronoUnit.DAYS), ZoneOffset.UTC), julianDayNumber.of(julianDayNumber.ofInstant(instant)).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofInstant(instant)).to(millis).toInstant(), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofInstant(instant)).to(julian).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), julianDayNumber.of(julianDayNumber.ofInstant(instant)).to(julianDayNumber).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofInstant(instant)).to(modifiedJulian).toInstant(), Objects.toString(l));
    });

    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofEpochDay(0L)).toInstant());
    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofEpochMilli(0L)).toInstant());
    assertEquals(Instant.EPOCH.truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofEpochSecond(0L)).toInstant());

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofEpochDay(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofEpochMilli(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofEpochSecond(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofInstant(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), julianDayNumber.of(julianDayNumber.ofJulian(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
  }
}
