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
package jp.furplag.sandbox.time.internal;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;
import jp.furplag.sandbox.time.Deamtiet;
import jp.furplag.sandbox.trebuchet.Trebuchet;

public abstract class Julian implements Deamtiet<Double> {

  /** {@inheritDoc} */
  @Override
  public Double ofEpochSecond(Long epochSecond) {
    return ofInstant(Trebuchet.Functions.orNot(epochSecond, (_epochSecond) -> Instant.ofEpochMilli(_epochSecond * 1000L)));
  }

  /** {@inheritDoc} */
  @Override
  public Double ofEpochMilli(Long epochMilli) {
    return Stream.of(Optional.ofNullable(epochMilli).orElseGet(System::currentTimeMillis))
      .mapToDouble((l) -> l * milliAsJulian)
      .mapToObj((d) -> Map.entry(d, d % milliAsJulian))
      .mapToDouble((m) -> m.getKey() - m.getValue() + (m.getValue() * 2 < milliAsJulian ? 0 : milliAsJulian))
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
}
