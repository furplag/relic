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
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import jp.furplag.sandbox.time.internal.Millis;
import jp.furplag.sandbox.time.internal.ModifiedJulian;
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
    Entity(Deamtiet<N> deamtiet, Instant instant) {
      this.deamtiet = Objects.requireNonNull(deamtiet);
      this.instant = Objects.requireNonNull(instant);
    }

    /**
     * method-chaining .
     *
     * @param <Another> type of the return value of an instantaneous point
     * @param another a {@link Deamtiet} to change the type of return value, may not be null
     * @return another type of {@link Deamtiet.Entity}
     */
    public <Another extends Number> Entity<Another> to(Deamtiet<Another> another) {
      return new Entity<>(another, instant) {};
    }

    /**
     * returns the number of days from 1970-01-01T00:00:00Z .
     *
     * @return the number of days from 1970-01-01T00:00:00Z
     */
    public long toEpochDay() {
      return toLocalDate(ZoneOffset.UTC).getLong(ChronoField.EPOCH_DAY);
    }

    /**
     * returns the number of seconds from 1970-01-01T00:00:00Z .
     *
     * @return the number of seconds from 1970-01-01T00:00:00Z
     */
    public long toEpochSecond() {
      return toInstant().truncatedTo(ChronoUnit.SECONDS).toEpochMilli() / 1000L;
    }

    /**
     * returns the value of an instantaneous point .
     *
     * @return an instantaneous point
     */
    public Instant toInstant() {
      return deamtiet.toInstant(value());
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

    /**
     * returns the value of an instantaneous point represented by the time scale of this .
     *
     * @return an instantaneous point represented by the time scale of this
     */
    public N value() {
      return deamtiet.ofInstant(instant);
    }
  }

  /** the days of julian year. */
  public static final double daysOfYearOfJulian = 365.25d;

  /** astronomical julian date of 1970-01-01T00:00:00.000Z. */
  public static final double epochAsJulianDate = 2_440_587.5d;

  /** astronomical julian date of 1582-10-15T00:00:00.000Z. */
  public static final double gregorianEpochAsJulianDate = 2_299_160.5d;

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
  static Deamtiet<Double> Julian = new Deamtiet<>() {

    /** {@inheritDoc} */
    @Override
    public Double ofEpochSecond(Long epochSecond) {
      return ofInstant(Trebuchet.Functions.orNot(epochSecond, (_epochSecond) -> Instant.ofEpochMilli(_epochSecond * 1000L)));
    }

    /** {@inheritDoc} */
    @Override
    public Double ofEpochMilli(Long epochMilli) {
      return Stream.of(Optional.ofNullable(epochMilli).orElseGet(System::currentTimeMillis))
        .mapToDouble((l) -> l * 1d / millisOfDay)
        .mapToObj((d) -> Map.entry(d, d % (1d / millisOfDay)))
        .mapToDouble((m) -> m.getKey() - m.getValue() + (m.getValue() < (.5d / millisOfDay) ? 0 : (1d / millisOfDay)))
        .flatMap((d) -> DoubleStream.of(d, epochAsJulianDate))
        .sum();
    }

    /** {@inheritDoc} */
    @Override
    public Double ofInstant(Instant instant) {
      return ofEpochMilli(Trebuchet.Functions.orNot(instant, Instant::toEpochMilli));
    }
    /** {@inheritDoc} */
    @Override
    public Double ofJulian(Double julianDate) {
      return Objects.requireNonNullElse(julianDate, ofEpochMilli(null));
    }

    /** {@inheritDoc} */
    @Override
    public Instant toInstant(Double instantValue) {
      return Instant.ofEpochMilli(Trebuchet.Functions.orElse(instantValue, (_julianDate) -> Stream.of(Objects.requireNonNull(_julianDate))
        .map((d) -> Map.entry(d - epochAsJulianDate, (d - epochAsJulianDate) % (1d / millisOfDay)))
        .mapToLong((m) -> Math.round((m.getKey() - m.getValue() + (m.getValue() < (.5d / millisOfDay) ? 0 : (1d / millisOfDay))) / (1d / millisOfDay)))
        .findAny().getAsLong(), System::currentTimeMillis));
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
      return Julian.toInstant(ofJulian(Trebuchet.Functions.orNot(instantValue, Long::doubleValue)).doubleValue()).truncatedTo(ChronoUnit.DAYS);
    }
  };

  /** an instant represented by epoch millis . */
  static final Deamtiet<Long> Millis = new Millis() {};

  /** an instant represented by modified julian day . */
  static final Deamtiet<Double> ModifiedJulian = new ModifiedJulian() {};

  /**
   * returns a simple wrapper of {@link Deamtiet this} .
   *
   * @param instantValue an instant value represented by the time scale of this, use current timestamp if null
   * @return {@link Deamtiet.Entity}
   */
  default Entity<N> of(N instantValue) {
    return new Entity<>(this, toInstant(instantValue)) {};
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
    return ofInstant(Trebuchet.Functions.orElse(epochSecond, (_epochSecond) -> Instant.ofEpochSecond(Objects.requireNonNull(epochSecond)), () -> Instant.now().truncatedTo(ChronoUnit.SECONDS)));
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
   * returns the value which converted as a type of {@link Instant} .
   *
   * @param instantValue an instantaneous point represented by the time scale of {@code this}, use current timestamp if null
   * @return the value which converted as a type of {@link Instant}
   */
  Instant toInstant(N instantValue);
}
