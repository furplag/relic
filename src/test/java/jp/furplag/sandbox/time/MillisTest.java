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

class MillisTest {

  @Test
  void test() {

    LongStream.rangeClosed(0, 100_000).forEach((l) -> {
      final Instant instant = Instant.parse("1996-01-23T12:34:56.789Z").plusMillis(l);

      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), Millis.toInstant(Millis.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay)), Objects.toString(l));
      assertEquals(instant, Millis.toInstant(Millis.ofEpochMilli(instant.toEpochMilli())), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), Millis.toInstant(Millis.ofEpochSecond(instant.toEpochMilli() / 1000L)), Objects.toString(l));
      assertEquals(instant, Millis.toInstant(Millis.ofInstant(instant)), Objects.toString(l));
      assertEquals(instant, Millis.toInstant(Millis.ofJulian(Julian.ofInstant(instant))), Objects.toString(l));
      assertEquals(instant, Millis.toInstant(Millis.of(Millis.ofEpochMilli(instant.toEpochMilli())).value()), Objects.toString(l));

      assertEquals(instant.toEpochMilli() / Deamtiet.millisOfDay, Millis.of(Millis.ofEpochMilli(instant.toEpochMilli())).toEpochDay(), Objects.toString(l));
      assertEquals(instant.toEpochMilli() / 1000L, Millis.of(Millis.ofEpochMilli(instant.toEpochMilli())).toEpochSecond(), Objects.toString(l));
      assertEquals(instant, Millis.of(Millis.ofEpochMilli(instant.toEpochMilli())).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), Millis.of(Millis.ofEpochMilli(instant.toEpochMilli())).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), Millis.of(Millis.ofEpochMilli(instant.toEpochMilli())).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(instant, Millis.of(Millis.ofInstant(instant)).to(Millis).toInstant(), Objects.toString(l));
      assertEquals(instant, Millis.of(Millis.ofInstant(instant)).to(Julian).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), Millis.of(Millis.ofInstant(instant)).to(JulianDayNumber).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(instant, Millis.of(Millis.ofInstant(instant)).to(ModifiedJulian).toInstant(), Objects.toString(l));
    });

    assertEquals(Instant.EPOCH, Millis.of(Millis.ofEpochDay(0L)).toInstant());
    assertEquals(Instant.EPOCH, Millis.of(Millis.ofEpochMilli(0L)).toInstant());
    assertEquals(Instant.EPOCH, Millis.of(Millis.ofEpochSecond(0L)).toInstant());

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), Millis.of(Millis.ofEpochDay(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Millis.of(Millis.ofEpochMilli(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Millis.of(Millis.ofEpochSecond(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Millis.of(Millis.ofInstant(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Millis.of(Millis.ofJulian(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
  }
}
