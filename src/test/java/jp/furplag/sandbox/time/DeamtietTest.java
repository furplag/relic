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

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class DeamtietTest {

  @Test
  void requireNonNull() {
    try {
      Deamtiet.of(null, null);
    } catch (Throwable ex) {
      assertEquals(NullPointerException.class, ex.getClass());
    }
    try {
      Deamtiet.of(null, System.currentTimeMillis());
    } catch (Throwable ex) {
      assertEquals(NullPointerException.class, ex.getClass());
    }
  }

  @Test
  void test() {
    assertEquals(Deamtiet.of(Deamtiet.Millis, null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.of(Deamtiet.Julian, null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.of(Deamtiet.JulianDayNumber, null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.JulianDayNumber.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.of(Deamtiet.ModifiedJulian, null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));

    assertEquals(System.currentTimeMillis(), Deamtiet.Millis.of(null).getValue(), 100d);
    assertEquals(System.currentTimeMillis(), Deamtiet.Julian.of(null).to(Deamtiet.Millis).getValue(), 100d);
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli(), Deamtiet.JulianDayNumber.of(null).to(Deamtiet.Millis).getValue());
    assertEquals(System.currentTimeMillis(), Deamtiet.ModifiedJulian.of(null).to(Deamtiet.Millis).getValue(), 100d);

    assertEquals(LocalDate.parse("1970-01-01"), Deamtiet.Millis.of(0L).toLocalDate(null));
    assertEquals(LocalDate.parse("1970-01-01"), Deamtiet.Julian.of(Deamtiet.epochAsJulianDate).toLocalDate(null));
    assertEquals(LocalDate.parse("1970-01-01"), Deamtiet.JulianDayNumber.of(((long) Deamtiet.epochAsJulianDate) + 1).toLocalDate(null));
    assertEquals(LocalDate.parse("1858-11-17"), Deamtiet.ModifiedJulian.of(0d).toLocalDate(null));

    assertEquals(LocalDateTime.parse("1970-01-01T00:00"), Deamtiet.Millis.of(0L).toLocalDateTime(ZoneId.of("UTC")));
    assertEquals(LocalDateTime.parse("1970-01-01T00:00"), Deamtiet.Julian.of(Deamtiet.epochAsJulianDate).toLocalDateTime(ZoneId.of("UTC")));
    assertEquals(LocalDateTime.parse("1970-01-01T00:00"), Deamtiet.JulianDayNumber.of(((long) Deamtiet.epochAsJulianDate) + 1).toLocalDateTime(ZoneId.of("UTC")));
    assertEquals(LocalDateTime.parse("1858-11-17T00:00"), Deamtiet.ModifiedJulian.of(0d).toLocalDateTime(ZoneId.of("UTC")));

    assertEquals(ZonedDateTime.now().toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli(), Deamtiet.Millis.of(null).to(Deamtiet.Julian).toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli());

    assertEquals(Instant.ofEpochSecond(0).truncatedTo(ChronoUnit.SECONDS), Deamtiet.Millis.of(Deamtiet.Millis.ofEpochSecond(0L)).toInstant());
    assertEquals(Instant.ofEpochSecond(System.currentTimeMillis() / 1000), Deamtiet.Millis.of(Deamtiet.Millis.ofEpochSecond(null)).toInstant());
    assertEquals(Instant.ofEpochSecond(System.currentTimeMillis() / 1000).truncatedTo(ChronoUnit.DAYS), Deamtiet.Millis.of(Deamtiet.Millis.ofEpochDay(null)).toInstant());
    assertEquals(Instant.ofEpochSecond(100000000).truncatedTo(ChronoUnit.SECONDS),  Deamtiet.Millis.of(Deamtiet.Millis.ofEpochSecond(100000000L)).toInstant());
    assertEquals(Instant.ofEpochMilli(0).plus(10000, ChronoUnit.DAYS),  Deamtiet.Millis.of(Deamtiet.Millis.ofEpochDay(10000L)).toInstant());

    assertEquals(100000000, Deamtiet.Millis.of(Deamtiet.Millis.ofInstant(Instant.parse("1973-03-03T09:46:40Z"))).toEpochSecond());
    assertEquals(10000, Deamtiet.Millis.of(Deamtiet.Millis.ofInstant(Instant.parse("1997-05-19T00:00:00Z"))).toEpochDay());
  }
}
