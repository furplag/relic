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
package jp.furplag.sandbox.tuple;

import java.util.Map;
import java.util.Objects;

/**
 * an immutable pair consisting of two object elements .
 *
 * @author furplag
 *
 * @param <K> the key element type
 * @param <V> the value element type
 */
public interface Tag<K, V> extends Map.Entry<K, V> {

  /**
   * returns the type of the value .
   *
   * @return the type of the value
   */
  @SuppressWarnings({"unchecked"})
  default Class<V> getValueType() {/* @formatter:off */return (Class<V>) getValue().getClass();/* @formatter:on */}

  /**
   * throws {@code UnsupportedOperationException} .
   * <p>
   * the {@link Tag} is immutable, so this operation is not supported .
   *
   * @param value the value to set
   * @return never
   * @throws UnsupportedOperationException as this operation is not supported
   */
  @Override
  default V setValue(V value) {/* @formatter:off */throw new UnsupportedOperationException();/* @formatter:on */}

  /**
   * create a new instance of {@link Tag} .
   *
   * @param <K> the key element type
   * @param <V> the value element type
   * @param key
   * @param value
   * @return a new instance of {@link Tag}
   */
  static <K, V> Tag<K, V> entry(final K key, final V value) {/* @formatter:off */
    return new Tag<>() {
      {Objects.requireNonNull(key);}
      @Override public K getKey() {return key;}
      @Override public V getValue() {return value;}
    };
  /* @formatter:on */}
}
