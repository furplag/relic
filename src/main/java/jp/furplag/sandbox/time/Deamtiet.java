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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import jp.furplag.sandbox.trebuchet.Trebuchet;

/**
 * converting between each instant values represented by some of (different) time scale,
 * e.g.) epoch millis, unix date, julian date and something .
 *
 * @author furplag
 * @param <N> type of the return value of an instantaneous point
 *
 */
public interface Deamtiet<N extends Number> {

  /**
   * a simple wrapper for {@link Deamtiet} that enable to method-chaining .
   *
   * @author furplag
   * @param <N> type of the return value of an instantaneous point
   */
  static abstract class Entity<N extends Number> {

    /** {@link Deamtiet} . */
    final Deamtiet<N> deamtiet;

    /** an instantaneous point . */
    final Instant instant;

    /**
     * @param deamtiet {@link Deamtiet}, may not be null
     * @param instant an instantaneous point, may not be null
     */
    private Entity(Deamtiet<N> deamtiet, Instant instant) {
      this.deamtiet = Objects.requireNonNull(deamtiet);
      this.instant = Objects.requireNonNull(instant);
    }

    /**
     * returns the value of an instantaneous point represented by the time scale of this {@code deamtiet} .
     *
     * @return an instantaneous point represented by the time scale of this {@code deamtiet}
     */
    public N getValue() {
      return deamtiet.ofInstant(instant);
    }

    /**
     * method-chaining .
     *
     * @param <ANOTHER> type of the return value of an instantaneous point
     * @param another a {@link Deamtiet} to change the type of return value, may not be null
     * @return another type of {@link Deamtiet.Entity}
     */
    public <ANOTHER extends Number> Entity<ANOTHER> to(Deamtiet<ANOTHER> another) {
      return new Entity<>(another, instant) {};
    }

    /**
     * returns the number of days from 1970-01-01T00:00:00Z .
     *
     * @return the number of days from 1970-01-01T00:00:00Z
     */
    public long toEpochDay() {
      return deamtiet.toEpochDay(getValue());
    }

    /**
     * returns the number of seconds from 1970-01-01T00:00:00Z .
     *
     * @return the number of seconds from 1970-01-01T00:00:00Z
     */
    public long toEpochSecond() {
      return deamtiet.toEpochSecond(getValue());
    }

    /**
     * returns the value of an instantaneous point .
     *
     * @return an instantaneous point
     */
    public Instant toInstant() {
      return instant;
    }

    /**
     * shorthand for {@link LocalDate#ofInstant(Instant, ZoneId)} .
     *
     * @param zone the time-zone, which may be an offset, use {@link ZoneId#systemDefault()} if null
     * @return {@link LocalDate}
     */
    public LocalDate toLocalDate(ZoneId zone) {
      return LocalDate.ofInstant(instant, Objects.requireNonNullElseGet(zone, ZoneId::systemDefault));
    }

    /**
     * shorthand for {@link LocalDateTime#ofInstant(Instant, ZoneId)} .
     *
     * @param zone the time-zone, which may be an offset, use {@link ZoneId#systemDefault()} if null
     * @return {@link LocalDateTime}
     */
    public LocalDateTime toLocalDateTime(ZoneId zone) {
      return LocalDateTime.ofInstant(instant, Objects.requireNonNullElseGet(zone, ZoneId::systemDefault));
    }
  }

  /** the days of julian year. */
  public static final double daysOfYearOfJulian = 365.25d;

  /** astronomical julian date of 1970-01-01T00:00:00.000Z. */
  public static final double epochAsJulianDate = 2_440_587.5d;

  /** astronomical julian date of 1582-10-15T00:00:00.000Z. */
  public static final double gregorianEpochAsJulianDate = 2299160.5;

  /** the epoch millis from 1582-10-15T00:00:00.000Z. */
  public static final long gregorianEpochAsMillis = -12_219_292_800_000L;

  /** delta of the days of a month in the lunar. */
  public static final double incrementOfSynodicMonth = 2.162E-9d;

  /** astronomical julian date of 2001-01-01T12:00:00.000Z. */
  public static final double j2000 = 2_451_545.0d;

  /** the epoch millis from -4713-11-24T12:00:00.000Z. */
  public static final long julianEpochAsMillis = -210_866_760_000_000L;

  /** the millis of one day. */
  public static final long millisOfDay = 864_000_00L;

  /** the epoch modified julian date of 1858-11-17T00:00:00.000Z. */
  public static final double modifiedJulianEpochAsJulianDate = 2_400_000.5d;

  /** an instant represented by astronomical julian day . */
  static final Deamtiet<Double> Julian = new Deamtiet<>() {

    /** {@inheritDoc} */
    @Override
    public Double ofEpochMilli(Long epochMilli) {
      return Objects.requireNonNullElseGet(epochMilli, System::currentTimeMillis) / ((double) millisOfDay) + epochAsJulianDate;
    }

    /** {@inheritDoc} */
    @Override
    public Double ofInstant(Instant instant) {
      return ofEpochMilli(Objects.requireNonNullElseGet(instant, Instant::now).toEpochMilli());
    }

    /** {@inheritDoc} */
    @Override
    public Double ofJulian(Double julianDate) {
      return Objects.requireNonNullElse(julianDate, ofEpochMilli(null));
    }

    /** {@inheritDoc} */
    @Override
    public <R extends Number> R to(Deamtiet<R> another, Double julianDate) {
      return Objects.requireNonNull(another).ofInstant(toInstant(julianDate));
    }

    /** {@inheritDoc} */
    @Override
    public Instant toInstant(Double instantValue) {
      return Instant.ofEpochMilli((long) ((Objects.requireNonNullElse(instantValue, ofJulian(null)) - epochAsJulianDate) * millisOfDay));
    }
  };

  /** an instant represented by astronomical julian day number . */
  static final Deamtiet<Long> JulianDayNumber = new Deamtiet<>() {

    /** {@inheritDoc} */
    @Override
    public Long ofEpochMilli(Long epochMilli) {
      return (long) (Julian.ofEpochMilli(epochMilli) + .5d);
    }

    /** {@inheritDoc} */
    @Override
    public Long ofInstant(Instant instant) {
      return (long) (Julian.ofInstant(instant) + .5d);
    }

    /** {@inheritDoc} */
    @Override
    public Long ofJulian(Double julianDate) {
      return (long) (Julian.ofJulian(julianDate) + .5d);
    }

    /** {@inheritDoc} */
    @Override
    public Instant toInstant(Long instantValue) {
      return Trebuchet.Functions.orElse(instantValue, (_instantValue) -> Instant.ofEpochMilli(Millis.ofJulian((double) ofJulian((double) _instantValue))), () -> Instant.now()).truncatedTo(ChronoUnit.DAYS);
    }
  };

  /** an instant represented by epoch millis . */
  static final Deamtiet<Long> Millis = new Deamtiet<>() {

    /** {@inheritDoc} */
    @Override
    public Long ofEpochMilli(Long epochMilli) {
      return Objects.requireNonNullElseGet(epochMilli, System::currentTimeMillis);
    }

    /** {@inheritDoc} */
    @Override
    public Long ofInstant(Instant instant) {
      return Objects.requireNonNullElseGet(instant, Instant::now).toEpochMilli();
    }

    /** {@inheritDoc} */
    @Override
    public Long ofJulian(Double julianDate) {
      return (long) ((Objects.requireNonNullElse(julianDate, Julian.ofJulian(null)) - epochAsJulianDate) * millisOfDay);
    }

    /** {@inheritDoc} */
    @Override
    public Instant toInstant(Long instantValue) {
      return Instant.ofEpochMilli(ofEpochMilli(instantValue));
    }
  };

  /** an instant represented by modified julian day . */
  static final Deamtiet<Double> ModifiedJulian = new Deamtiet<>() {

    /** {@inheritDoc} */
    @Override
    public Double ofEpochMilli(Long epochMilli) {
      return Julian.ofEpochMilli(epochMilli) - modifiedJulianEpochAsJulianDate;
    }

    /** {@inheritDoc} */
    @Override
    public Double ofInstant(Instant instant) {
      return Julian.ofInstant(instant) - modifiedJulianEpochAsJulianDate;
    }

    /** {@inheritDoc} */
    @Override
    public Double ofJulian(Double julianDate) {
      return Julian.ofJulian(julianDate) - modifiedJulianEpochAsJulianDate;
    }

    /** {@inheritDoc} */
    @Override
    public Instant toInstant(Double instantValue) {
      return Instant.ofEpochMilli(Millis.ofJulian((Objects.requireNonNullElse(instantValue, ofJulian(null)) + modifiedJulianEpochAsJulianDate)));
    }
  };

  /**
   * returns a simple wrapper of {@link Deamtiet this} .
   *
   * @param <N> type of the return value of an instantaneous point
   * @param deamtiet {@link Deamtiet}, may not be null
   * @param instantValue an instant value represented by the time scale of {@code deamtiet}, use current timestamp if null
   * @return {@link Deamtiet.Entity}
   */
  static <N extends Number> Entity<N> of(Deamtiet<N> deamtiet, N instantValue) {
    return new Entity<>(deamtiet, deamtiet.toInstant(instantValue)) {};
  }

  /**
   * returns a simple wrapper of {@link Deamtiet this} .
   *
   * @param instantValue an instant value represented by the time scale of this, use current timestamp if null
   * @return {@link Deamtiet.Entity}
   */
  default Entity<N> of(N instantValue) {
    return new Entity<>(this, this.toInstant(instantValue)) {};
  }

  /**
   * returns an instant value represented by the time scale of this class .
   *
   * @param epochDay the number of days from 1970-01-01T00:00:00Z, use current timestamp if null
   * @return an instant value represented by the time scale of this class
   */
  default N ofEpochDay(Long epochDay) {
    return ofInstant(Trebuchet.Functions.orElse(epochDay, (_epochDay) -> Instant.EPOCH.plus(_epochDay, ChronoUnit.DAYS), () -> Instant.now().truncatedTo(ChronoUnit.DAYS)));
  }

  /**
   * returns an instant value represented by the time scale of this class .
   *
   * @param epochMilli the number of milliseconds from 1970-01-01T00:00:00Z, use current timestamp if null
   * @return an instant value represented by the time scale of this class
   */
  N ofEpochMilli(Long epochMilli);

  /**
   * returns an instant value represented by the time scale of this class .
   *
   * @param epochSecond the number of seconds from 1970-01-01T00:00:00Z, use current timestamp if null
   * @return an instant value represented by the time scale of this class
   */
  default N ofEpochSecond(Long epochSecond) {
    return ofInstant(Trebuchet.Functions.orElse(epochSecond, Instant::ofEpochSecond, () -> Instant.now().truncatedTo(ChronoUnit.SECONDS)));
  }

  /**
   * returns an instant value represented by the time scale of this class .
   *
   * @param instant {@link Instant}, use {@link Instant#now()} if null
   * @return an instant value represented by the time scale of this class
   */
  N ofInstant(Instant instant);

  /**
   * returns an instant value represented by the time scale of this class .
   *
   * @param julianDate a julian date, use current timestamp if null
   * @return an instant value represented by the time scale of this class
   */
  N ofJulian(Double julianDate);

  /**
   * returns the value which converted as a type of specified {@link Deamtiet} .
   *
   * @param <R> the type of return value
   * @param another {@link Deamtiet} to convert, may not be null
   * @param instantValue an instantaneous point represented by the time scale of {@code this}, use current timestamp if null
   * @return the value which converted as a type of specified {@link Deamtiet}
   */
  default <R extends Number> R to(Deamtiet<R> another, N instantValue) {
    return Objects.requireNonNull(another).ofInstant(toInstant(instantValue));
  }

  /**
   * returns the number of days from 1970-01-01T00:00:00Z .
   *
   * @param instantValue an instantaneous point represented by the time scale of {@code this}, use current timestamp if null
   * @return the number of days from 1970-01-01T00:00:00Z
   */
  default long toEpochDay(N instantValue) {
    return toInstant(instantValue).truncatedTo(ChronoUnit.DAYS).toEpochMilli() / millisOfDay;
  }

  /**
   * returns the number of seconds from 1970-01-01T00:00:00Z .
   *
   * @param instantValue an instantaneous point represented by the time scale of {@code this}, use current timestamp if null
   * @return the number of seconds from 1970-01-01T00:00:00Z
   */
  default long toEpochSecond(N instantValue) {
    return toInstant(instantValue).truncatedTo(ChronoUnit.SECONDS).toEpochMilli() / 1000L;
  }

  /**
   * returns the value which converted as a type of {@link Instant} .
   *
   * @param instantValue an instantaneous point represented by the time scale of {@code this}, use current timestamp if null
   * @return the value which converted as a type of {@link Instant}
   */
  Instant toInstant(N instantValue);
}
