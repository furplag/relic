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

/**
 * an instantaneous point represented by epoch millis .
 *
 * @author furplag
 *
 */
public abstract class Millis implements Deamtiet<Long> {

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
    return Julian.toInstant(julianDate).toEpochMilli();
  }

  /** {@inheritDoc} */
  @Override
  public Instant toInstant(Long instantValue) {
    return Instant.ofEpochMilli(ofEpochMilli(instantValue));
  }
}
