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
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import jp.furplag.sandbox.function.Trebuchet;
import jp.furplag.sandbox.reflect.unsafe.TheUnsafe;
import jp.furplag.sandbox.stream.Streamr;

/**
 * handle class member whether protected ( or invisible ) WITHOUT using sun.misc.Unsafe .
 *
 * @author furplag
 *
 */
public final class SavageReflection {

  /** shorthand for {@link Modifier#isStatic(int)} . */
  public static final Predicate<Field> isStatic = (f) -> Modifier.isStatic(f.getModifiers());
  /** shorthand for {@link Set#contains(Object)} . */
  public static final BiPredicate<Set<String>, Field> exclusions = (set, f) -> set != null && set.contains(f.getName());

  /**
   * read field value whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return value of field
   */
  private static Object readField(final Object mysterio, final Field field) {
    return Trebuchet.orElse((Trebuchet.ThrowableBiFunction<Object, Field, Object>) (o, f) -> TheUnsafe.getInstance().get(o, f), (x, e) -> null).apply(mysterio, field);
  }

  /**
   * read field value whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @return value of field
   */
  public static Object get(final Object mysterio, final Field field) {
    return !Reflections.isAssignable(mysterio, field) ? null : Trebuchet.orElse(((Trebuchet.ThrowableBiFunction<Object, Field, Object>) (o, f) -> readField(o, f)), (x, e) -> null).apply(mysterio, field);
  }

  /**
   * read field value whether protected ( or invisible ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param fieldName the name of field
   * @return value of field
   */
  public static Object get(final Object mysterio, final String fieldName) {
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
  public static boolean set(final Object mysterio, final Field field, final Object value) {
    // @formatter:off
    return
      Reflections.isAssignable(mysterio, field, value) &&
      Trebuchet.orElse(((Trebuchet.ThrowableBiFunction<Field, Object, Boolean>) (f, v) -> {
        writeField(mysterio, f, v);

        return Objects.equals(String.class.equals(f.getType()) ? Objects.toString(value) : value, get(mysterio, f));
      }), (x, v) -> false).apply(Reflections.conciliation(field), value);
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
  public static boolean set(final Object mysterio, final String fieldName, final Object value) {
    return set(mysterio, Reflections.getField(mysterio, fieldName), value);
  }

  /**
   * update field value whether finalized ( or invisible, and static ) .
   *
   * @param mysterio {@link Class} or the instance
   * @param field {@link Field}
   * @param value the value for update
   */
  private static void writeField(final Object mysterio, final Field field, final Object value) {
    Trebuchet.orElse((Field f, Object v) -> TheUnsafe.getInstance().set(mysterio, f, v), (x, v) -> x.printStackTrace()).accept(field, value);
  }

  /**
   * read field(s) value of the instance whether protected ( or invisible ) .
   *
   * @param instance the instance
   * @param excludes the name of field which you want to except from result.
   * @return {@link LinkedHashMap} &lt;{@link String}, {@link Object}&gt;
   */
  public static Map<String, Object> read(final Object instance, final String... excludes) {
    final Set<String> _excludes = Streamr.stream(excludes).collect(Collectors.toCollection(HashSet::new));
    // @formatter:off
    return Collections.unmodifiableMap(Streamr.stream(Reflections.getFields(instance instanceof Class ? null : instance)).filter(isStatic.negate()).filter((f) -> !exclusions.test(_excludes, f))
      .collect(
        LinkedHashMap::new
      , (map, field) -> map.putIfAbsent(field.getName(), Trebuchet.orElse((x) ->SavageReflection.get(instance, field), (e, x) -> null).apply(field))
      , LinkedHashMap::putAll)
    // @formatter:on
    );
  }
}
