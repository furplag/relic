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
import org.junit.jupiter.api.Test;

class JulianTest {

  @Test
  void test() {
    assertEquals(Millis.to(Julian, null), Julian.ofEpochMilli(null), .1d);
    assertEquals(Millis.to(Julian, null), Julian.ofJulian(null), .1d);
    assertEquals(Millis.to(Julian, null), Julian.ofInstant(null), .1d);

    assertEquals(Millis.to(Julian, System.currentTimeMillis()), Julian.ofEpochMilli(System.currentTimeMillis()), .1d);
    assertEquals(Millis.to(Julian, System.currentTimeMillis()), Julian.ofJulian(Millis.to(Julian, null)), .1d);
    assertEquals(Millis.to(Julian, System.currentTimeMillis()), Julian.ofInstant(Instant.now()), .1d);

    assertEquals(Deamtiet.epochAsJulianDate, Deamtiet.Julian.ofEpochMilli(0L));
    assertEquals(Deamtiet.epochAsJulianDate, Deamtiet.Julian.ofInstant(Instant.parse("1970-01-01T00:00:00.000Z")));

    assertEquals(Instant.now().toEpochMilli(), Julian.toInstant(null).toEpochMilli(), 100d);
    assertEquals(Instant.parse("-4713-11-24T12:00:00.000Z"), Julian.toInstant(0d));

    assertEquals(System.currentTimeMillis(), Julian.to(Millis, null), 100d);
    assertEquals(Julian.ofJulian(null), Julian.to(Julian, null), .1d);
    assertEquals(JulianDayNumber.ofJulian(null), Julian.to(JulianDayNumber, null));
    assertEquals(ModifiedJulian.ofJulian(null), Julian.to(ModifiedJulian, null), .1d);
  }
}
