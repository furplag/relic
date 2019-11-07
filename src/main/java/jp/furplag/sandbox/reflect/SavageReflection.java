/**
 * Copyright (C) 2018+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package jp.furplag.sandbox.reflect;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;
import jp.furplag.sandbox.reflect.unsafe.TheUnsafe;
import jp.furplag.sandbox.stream.Streamr;
import jp.furplag.sandbox.trebuchet.Trebuchet;

/**
 * handles class member whether protected ( or invisible ) .
 *
 * @author furplag
 *
 */
public interface SavageReflection {

  /**
   * read field value whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return value of field
   */
  static Object get(final Object mysterio, final Field field) {/* @formatter:off */
    return !Reflections.isAssignable(mysterio, field) ? null : Trebuchet.Functions.orNot(mysterio, field, (t, u) -> SavageReflection.readField(t, u));
  /* @formatter:on */}

  /**
   * read field value whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param fieldName the name of field
   * @return value of field
   */
  static Object get(final Object mysterio, final String fieldName) {
    return get(mysterio, Reflections.getField(mysterio, fieldName));
  }

  /**
   * read field(s) value of the instance whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param excludes the name of field which you want to except from result
   * @return {@link LinkedHashMap} &lt;{@link String}, {@link Object}&gt;
   */
  static Map<String, Object> read(final Object mysterio, final String... excludes) {
    return mappingFields(readFields(mysterio, excludes), mysterio);
  }

  /**
   * read field(s) value of the instance whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param excludes the name of field which you want to except from result
   * @return {@link Stream} of field which member of {@code mysterio}
   */
  private static Stream<Field> readFields(final Object mysterio,
      final String... excludes) {/* @formatter:off */
    return Streamr.stream(Reflections.getFields(mysterio))
      .filter(mysterio instanceof Class ? Reflections::isStatic : Predicate.not(Reflections::isStatic))
      .filter((t) -> Trebuchet.Predicates.orNot(t, (x) -> !Streamr.collect(excludes).contains(x.getName())));
  /* @formatter:on */}

  /**
   * read field(s) value of the instance whether protected ( or invisible ) .
   *
   * @param fields {@link Stream} of field which member of {@code mysterio}
   * @param mysterio {@link Class} or the instance
   * @return {@link LinkedHashMap} &lt;{@link String}, {@link Object}&gt;
   *//* @formatter:off */
  private static Map<String, Object> mappingFields(final Stream<Field> fields, final Object mysterio) {
    return Streamr.stream(fields).collect(
      LinkedHashMap::new
    , (map, t) -> map.putIfAbsent(t.getName(), readField(mysterio, t))
    , LinkedHashMap::putAll);
  }/* @formatter:on */

  /**
   * read field value whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return value of field
   */
  private static Object readField(final Object mysterio, final Field field) {
    return Trebuchet.Functions.orNot(mysterio, field, TheUnsafe::get);
  }

  /**
   * update field value whether finalized ( or invisible, and static ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @param value the value for update
   * @return true if the field update successfully
   *//* @formatter:off */
  static boolean set(final Object mysterio, final Field field, final Object value) {
    return Reflections.isAssignable(mysterio, field, value) && Trebuchet.Predicates.orNot(mysterio, field, value, TheUnsafe::set);
  }/* @formatter:on */

  /**
   * update field value whether finalized ( or invisible, and static ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param fieldName the name of field
   * @param value the value for update
   * @return true if the field update successfully
   */
  static boolean set(final Object mysterio, final String fieldName, final Object value) {
    return set(mysterio, Reflections.getField(mysterio, fieldName), value);
  }
}
