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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

class DeamtietTest {

  @Test
  void paintItGreen() {
    abstract class Invalid implements Deamtiet<Integer> {/* @formatter:off */
      @Override
      public Deamtiet.Entity<Integer> of(Integer instantValue) {
        return new Deamtiet.Entity<Integer>(null, null) {};
      };
      @Override public Integer ofEpochMilli(Long epochMilli) { return null; }
      @Override public Integer ofInstant(Instant instant) { return null; }
      @Override public Integer ofJulian(Double julianDate) { return null; }
      @Override public Instant toInstant(Integer instantValue) { return null; }
    /* @formatter:on */};
    abstract class InvalidII extends Invalid {
      @Override
      public Deamtiet.Entity<Integer> of(Integer instantValue) {
        return new Deamtiet.Entity<Integer>(null, Instant.now()) {};
      };
    };
    abstract class InvalidIII extends Invalid {
      @Override
      public Deamtiet.Entity<Integer> of(Integer instantValue) {
        return new Deamtiet.Entity<Integer>(this, null) {};
      };
    };

    try {
      new Invalid(){}.of(1).toInstant();
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }

    try {
      new InvalidII(){}.of(1).toInstant();
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }

    try {
      new InvalidIII(){}.of(1).toInstant();
      fail("there must raise NullPointerException .");
    } catch (Exception ex) {
      assertTrue(ex instanceof NullPointerException);
    }
  }

  @Test
  void nulls() {
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Instant.now().truncatedTo(ChronoUnit.DAYS), Deamtiet.JulianDayNumber.of(null).toInstant());
    assertEquals(Instant.now().truncatedTo(ChronoUnit.SECONDS), Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));

    assertEquals(Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.DAYS), Deamtiet.JulianDayNumber.of(null).toInstant());
    assertEquals(Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));

    assertEquals(Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.DAYS), Deamtiet.JulianDayNumber.of(null).toInstant());
    assertEquals(Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));

    assertEquals(Deamtiet.JulianDayNumber.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Deamtiet.JulianDayNumber.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.DAYS));
    assertEquals(Deamtiet.JulianDayNumber.of(null).toInstant().truncatedTo(ChronoUnit.DAYS), Deamtiet.JulianDayNumber.of(null).toInstant());
    assertEquals(Deamtiet.JulianDayNumber.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.DAYS));

    assertEquals(Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Millis.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
    assertEquals(Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.DAYS), Deamtiet.JulianDayNumber.of(null).toInstant());
    assertEquals(Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS), Deamtiet.ModifiedJulian.of(null).toInstant().truncatedTo(ChronoUnit.SECONDS));
  }

  @Test
  void defaults() {
    assertEquals(Instant.EPOCH, Deamtiet.Millis.of(0L).toInstant());

    assertEquals(Instant.EPOCH, Deamtiet.Julian.of(Deamtiet.epochAsJulianDate).toInstant());
    assertEquals(Instant.ofEpochMilli(Deamtiet.julianEpochAsMillis), Deamtiet.Julian.of(0d).toInstant());

    assertEquals(Deamtiet.Julian.of(null).toInstant().truncatedTo(ChronoUnit.DAYS), Deamtiet.JulianDayNumber.of(null).toInstant());

    assertEquals(Instant.EPOCH, Deamtiet.JulianDayNumber.of((long) (Deamtiet.epochAsJulianDate + .5d)).toInstant());
    assertEquals(Instant.ofEpochMilli(Deamtiet.julianEpochAsMillis).truncatedTo(ChronoUnit.DAYS), Deamtiet.JulianDayNumber.of(0L).toInstant());

    assertEquals(Instant.EPOCH, Deamtiet.ModifiedJulian.of(Deamtiet.epochAsJulianDate - Deamtiet.modifiedJulianEpochAsJulianDate).toInstant());
    assertEquals(Instant.parse("1858-11-17T00:00:00Z"), Deamtiet.ModifiedJulian.of(0d).toInstant());
  }
}
