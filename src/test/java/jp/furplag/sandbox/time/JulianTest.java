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

class JulianTest {

  @Test
  void test() {

    LongStream.rangeClosed(0, 100_000).forEach((l) -> {
      final Instant instant = Instant.parse("1996-01-23T12:34:56.789Z").plusMillis(l);

      assertEquals(instant.truncatedTo(ChronoUnit.DAYS), julian.toInstant(julian.ofEpochDay(instant.toEpochMilli() / Deamtiet.millisOfDay)), Objects.toString(l));
      assertEquals(instant, julian.toInstant(julian.ofEpochMilli(instant.toEpochMilli())), Objects.toString(l));
      assertEquals(instant.truncatedTo(ChronoUnit.SECONDS), julian.toInstant(julian.ofEpochSecond(instant.toEpochMilli() / 1000L)), Objects.toString(l));
      assertEquals(instant, julian.toInstant(julian.ofInstant(instant)), Objects.toString(l));
      assertEquals(instant, julian.toInstant(julian.of(julian.ofEpochMilli(instant.toEpochMilli())).value()), Objects.toString(l));

      assertEquals(instant.toEpochMilli() / Deamtiet.millisOfDay, julian.of(julian.ofEpochMilli(instant.toEpochMilli())).toEpochDay(), Objects.toString(l));
      assertEquals(instant.toEpochMilli() / 1000L, julian.of(julian.ofEpochMilli(instant.toEpochMilli())).toEpochSecond(), Objects.toString(l));
      assertEquals(instant, julian.of(julian.ofEpochMilli(instant.toEpochMilli())).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), julian.of(julian.ofEpochMilli(instant.toEpochMilli())).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(LocalDateTime.ofInstant(instant, ZoneOffset.UTC), julian.of(julian.ofEpochMilli(instant.toEpochMilli())).toLocalDateTime(ZoneOffset.UTC), Objects.toString(l));

      assertEquals(instant, julian.of(julian.ofInstant(instant)).to(millis).toInstant(), Objects.toString(l));
      assertEquals(instant, julian.of(julian.ofInstant(instant)).to(julian).toInstant(), Objects.toString(l));
      assertEquals(LocalDate.ofInstant(instant, ZoneOffset.UTC), julian.of(julian.ofInstant(instant)).to(julianDayNumber).toLocalDate(ZoneOffset.UTC), Objects.toString(l));
      assertEquals(instant, julian.of(julian.ofInstant(instant)).to(modifiedJulian).toInstant(), Objects.toString(l));
    });

    assertEquals(Instant.EPOCH, julian.of(julian.ofEpochDay(0L)).toInstant());
    assertEquals(Instant.EPOCH, julian.of(julian.ofEpochMilli(0L)).toInstant());
    assertEquals(Instant.EPOCH, julian.of(julian.ofEpochSecond(0L)).toInstant());

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), julian.of(julian.ofEpochDay(null)).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), julian.of(julian.ofEpochMilli(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), julian.of(julian.ofEpochSecond(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), julian.of(julian.ofInstant(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), julian.of(julian.ofJulian(null)).toInstant().truncatedTo(ChronoUnit.SECONDS));
  }
}
