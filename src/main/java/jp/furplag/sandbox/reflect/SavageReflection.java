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

package jp.furplag.sandbox.reflect;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import jp.furplag.function.Suppressor;
import jp.furplag.sandbox.reflect.unsafe.TheUnsafe;
import jp.furplag.sandbox.stream.Streamr;

/**
 * handles class member whether protected ( or invisible ) .
 *
 * @author furplag
 *
 */
public interface SavageReflection {

  /** shorthand for {@link Set#contains(Object)} . */
  static final BiPredicate<Set<String>, Field> exclusions = (set, f) -> set != null && set.contains(f.getName());

  /**
   * read field value whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return value of field
   */
  private static Object readField(final Object mysterio, final Field field) {
    return Suppressor.orNull(mysterio, field, TheUnsafe::get);
  }

  /**
   * read field value whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return value of field
   */
  static Object get(final Object mysterio, final Field field) {
    // @formatter:off
    return !Reflections.isAssignable(mysterio, field) ? null :
      Suppressor.orNull(mysterio, field, SavageReflection::readField);
    // @formatter:on
  }

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
   * update field value whether finalized ( or invisible, and static ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @param value the value for update
   * @return true if the field update successfully
   */
  static boolean set(final Object mysterio, final Field field, final Object value) {
    // @formatter:off
    return Reflections.isAssignable(mysterio, field, value) &&
        Suppressor.isCorrect(mysterio, field, (o, f) -> TheUnsafe.set(o, f, value));
    // @formatter:on
  }

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

  /**
   * read field(s) value of the instance whether protected ( or invisible ) .
   *
   * @param object the instance
   * @param excludes the name of field which you want to except from result.
   * @return {@link LinkedHashMap} &lt;{@link String}, {@link Object}&gt;
   */
  static Map<String, Object> read(final Object object, final String... excludes) {
    final Set<String> _excludes = Streamr.stream(excludes).collect(Collectors.toCollection(HashSet::new));
    // @formatter:off
    return Collections.unmodifiableMap(Streamr.stream(Reflections.getFields(object instanceof Class ? null : object))
      .filter(Reflections.isStatic.negate().and((f) -> exclusions.negate().test(_excludes, f)))
      .collect(
        LinkedHashMap::new
      , (map, field) -> map.putIfAbsent(field.getName(), Suppressor.orNull(object, field, SavageReflection::get))
      , LinkedHashMap::putAll)
    );
    // @formatter:on
  }
}
