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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class JulianDayNumberTest {

  @Test
  void test() {
    assertEquals(Millis.to(JulianDayNumber, null), JulianDayNumber.ofEpochMilli(null));
    assertEquals(Millis.to(JulianDayNumber, null), JulianDayNumber.ofJulian(null));
    assertEquals(Millis.to(JulianDayNumber, null), JulianDayNumber.ofInstant(null));

    assertEquals(Millis.to(JulianDayNumber, System.currentTimeMillis()), JulianDayNumber.ofEpochMilli(System.currentTimeMillis()));
    assertEquals(Millis.to(JulianDayNumber, System.currentTimeMillis()), JulianDayNumber.ofJulian(Millis.to(Julian, null)));
    assertEquals(Millis.to(JulianDayNumber, System.currentTimeMillis()), JulianDayNumber.ofInstant(Instant.now()));

    assertEquals((long) (Deamtiet.epochAsJulianDate + .5d), Deamtiet.JulianDayNumber.ofEpochMilli(0L));
    assertEquals((long) (Deamtiet.epochAsJulianDate + .5d), Deamtiet.JulianDayNumber.ofInstant(Instant.parse("1970-01-01T00:00:00.000Z")));

    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), JulianDayNumber.toInstant(null));
    assertEquals(Instant.parse("-4713-11-24T00:00:00.000Z"), JulianDayNumber.toInstant(0L));

    assertEquals(LocalDate.now().atStartOfDay(), LocalDateTime.ofInstant(Instant.ofEpochMilli(JulianDayNumber.to(Millis, null)), ZoneId.of("UTC")));
    assertEquals(LocalDate.now().atStartOfDay(), LocalDateTime.ofInstant(Julian.toInstant(JulianDayNumber.to(Julian, null)), ZoneId.of("UTC")));
    assertEquals(LocalDate.now().atStartOfDay(), LocalDateTime.ofInstant(JulianDayNumber.toInstant(JulianDayNumber.to(JulianDayNumber, null)), ZoneId.of("UTC")));
    assertEquals(LocalDate.now().atStartOfDay(), LocalDateTime.ofInstant(ModifiedJulian.toInstant(JulianDayNumber.to(ModifiedJulian, null)), ZoneId.of("UTC")));
  }
}
