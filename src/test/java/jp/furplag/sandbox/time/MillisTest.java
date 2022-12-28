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
import java.util.Objects;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;

class MillisTest {

  @Test
  void test() {

    LongStream.rangeClosed(0, 100_000).forEach((l) -> {
      final Instant instant = Instant.parse("1996-01-23T12:34:56.789Z").plusMillis(l);

      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), millis.toInstant(millis.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay)), Objects.toString(l));
      assertEquals(instant, millis.toInstant(millis.ofEpochMilli(instant.toEpochMilli())), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), millis.toInstant(millis.ofEpochSecond(instant.toEpochMilli() / 1000L)), Objects.toString(l));
      assertEquals(instant, millis.toInstant(millis.ofInstant(instant)), Objects.toString(l));
      assertEquals(instant, millis.toInstant(millis.ofJulian(julian.ofInstant(instant))), Objects.toString(l));
      assertEquals(instant, millis.toInstant(millis.of(millis.ofEpochMilli(instant.toEpochMilli())).value()), Objects.toString(l));

      assertEquals(instant.toEpochMilli() / Deamtiet.millisOfDay, millis.of(millis.ofEpochMilli(instant.toEpochMilli())).toEpochDay(), Objects.toString(l));
      assertEquals(instant.toEpochMilli() / 1000L, millis.of(millis.ofEpochMilli(instant.toEpochMilli())).toEpochSecond(), Objects.toString(l));
      assertEquals(instant, millis.of(millis.ofEpochMilli(instant.toEpochMilli())).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), millis.of(millis.ofEpochMilli(instant.toEpochMilli())).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), millis.of(millis.ofEpochMilli(instant.toEpochMilli())).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(instant, millis.of(millis.ofInstant(instant)).to(millis).toInstant(), Objects.toString(l));
      assertEquals(instant, millis.of(millis.ofInstant(instant)).to(julian).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), millis.of(millis.ofInstant(instant)).to(julianDayNumber).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(instant, millis.of(millis.ofInstant(instant)).to(modifiedJulian).toInstant(), Objects.toString(l));
    });

    assertEquals(Instant.EPOCH, millis.of(millis.ofEpochDay(0L)).toInstant());
    assertEquals(Instant.EPOCH, millis.of(millis.ofEpochMilli(0L)).toInstant());
    assertEquals(Instant.EPOCH, millis.of(millis.ofEpochSecond(0L)).toInstant());

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), millis.of(millis.ofEpochDay(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), millis.of(millis.ofEpochMilli(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), millis.of(millis.ofEpochSecond(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), millis.of(millis.ofInstant(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), millis.of(millis.ofJulian(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
  }
}
