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
import java.util.Objects;
import jp.furplag.sandbox.time.Deamtiet;
import jp.furplag.sandbox.trebuchet.Trebuchet;

/**
 * an instant represented by modified julian day .
 *
 * @author furplag
 *
 */
public abstract class ModifiedJulian implements Deamtiet<Double> {

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
    return Julian.toInstant(Trebuchet.Functions.orNot(instantValue, (_instantValue) -> Objects.requireNonNull(_instantValue) + modifiedJulianEpochAsJulianDate));
  }
}
