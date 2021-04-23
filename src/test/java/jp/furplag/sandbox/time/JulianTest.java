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

import static jp.furplag.sandbox.time.Deamtiet.Julian;
import static jp.furplag.sandbox.time.Deamtiet.JulianDayNumber;
import static jp.furplag.sandbox.time.Deamtiet.Millis;
import static jp.furplag.sandbox.time.Deamtiet.ModifiedJulian;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;

class JulianTest {

  @Test
  void test() {

    LongStream.rangeClosed(0, 100_000).forEach((l) -> {
      final Instant instant = Instant.parse("1996-01-23T12:34:56.789Z").plusMillis(l);

      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), Julian.toInstant(Julian.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay)), Objects.toString(l));
      assertEquals(instant, Julian.toInstant(Julian.ofEpochMilli(instant.toEpochMilli())), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), Julian.toInstant(Julian.ofEpochSecond(instant.toEpochMilli() / 1000L)), Objects.toString(l));
      assertEquals(instant, Julian.toInstant(Julian.ofInstant(instant)), Objects.toString(l));
      assertEquals(instant, Julian.toInstant(Julian.of(Julian.ofEpochMilli(instant.toEpochMilli())).value()), Objects.toString(l));

      assertEquals(instant.toEpochMilli() / Deamtiet.millisOfDay, Julian.of(Julian.ofEpochMilli(instant.toEpochMilli())).toEpochDay(), Objects.toString(l));
      assertEquals(instant.toEpochMilli() / 1000L, Julian.of(Julian.ofEpochMilli(instant.toEpochMilli())).toEpochSecond(), Objects.toString(l));
      assertEquals(instant, Julian.of(Julian.ofEpochMilli(instant.toEpochMilli())).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), Julian.of(Julian.ofEpochMilli(instant.toEpochMilli())).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), Julian.of(Julian.ofEpochMilli(instant.toEpochMilli())).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(instant, Julian.of(Julian.ofInstant(instant)).to(Millis).toInstant(), Objects.toString(l));
      assertEquals(instant, Julian.of(Julian.ofInstant(instant)).to(Julian).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), Julian.of(Julian.ofInstant(instant)).to(JulianDayNumber).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(instant, Julian.of(Julian.ofInstant(instant)).to(ModifiedJulian).toInstant(), Objects.toString(l));
    });

    assertEquals(Instant.EPOCH, Julian.of(Julian.ofEpochDay(0L)).toInstant());
    assertEquals(Instant.EPOCH, Julian.of(Julian.ofEpochMilli(0L)).toInstant());
    assertEquals(Instant.EPOCH, Julian.of(Julian.ofEpochSecond(0L)).toInstant());

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), Julian.of(Julian.ofEpochDay(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Julian.of(Julian.ofEpochMilli(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Julian.of(Julian.ofEpochSecond(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Julian.of(Julian.ofInstant(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Julian.of(Julian.ofJulian(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
  }
}
