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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import jp.furplag.sandbox.stream.Streamr;

/**
 * code snippet for accessing object .
 *
 * @author furplag
 *
 */
public interface Reflections {

  /**
   * {@link #isAssignable(Field, Object)} using this method if the value type is standard object .
   *
   * @param typeOfFiled type of the field
   * @param typeOfValue type of the value
   * @return true if the value is able to set the field, or returns false if that is not able to
   */
  private static boolean isAssignable(final Class<?> typeOfFiled, final Class<?> typeOfValue) {
    // @formatter:off
    return typeOfFiled == null ? false :
      typeOfFiled.isPrimitive() && typeOfValue == null ? false :
      (
        ClassUtils.isAssignable(typeOfValue, typeOfFiled) ||
        Arrays.stream(ClassUtils.primitivesToWrappers(typeOfFiled, typeOfValue)).allMatch(Number.class::isAssignableFrom)
      );
    // @formatter:on
  }

  /**
   * shorthand for {@link #getClass(Object)}.{@link Class#getSuperclass() getSuperclass()}.{@link Class#getSuperclass() getSuperclass()} ...
   *
   * @param mysterio {@link Class} or an instance of any {@link Object}. maybe null
   * @return the stream contains {@link Class} which ordering in like "[me, father, grandfather ...]" .
   */
  static Stream<Class<?>> familyze(final Object mysterio) {
    List<Class<?>> classes = new ArrayList<>();
    Class<?> clazz = getClass(mysterio);
    while (clazz != null) {
      classes.add(clazz);
      clazz = clazz.getSuperclass();
    }

    return classes.stream();
  }

  /**
   * this unnecessary code snippet exists only for {@link Object#getClass()} .
   *
   * @param mysterio {@link Class} or an instance of any {@link Object}. maybe null
   * @return {@link java.lang.Class}, or null if {@code mysterio} is null
   */
  static Class<?> getClass(final Object mysterio) {
    // @formatter:off
    return mysterio == null ? null :
      (mysterio instanceof Class) ? ((Class<?>) mysterio) :
      mysterio.getClass();
    // @formatter:on
  }

  /**
   * {@link Class#getDeclaredField(String)} with deep finder .
   *
   * @param mysterio {@link Class} or an instance of any {@link Object}. maybe null
   * @param fieldName the name of field
   * @return {@link Field}, or null if the field not found
   */
  static Field getField(final Object mysterio, final String fieldName) {
    // @formatter:off
    return Streamr.firstOf(Streamr.stream(getFields(mysterio)), (f) -> StringUtils.equals(fieldName, f.getName()));
    // @formatter:on
  }

  /**
   * {@link Class#getDeclaredFields()} with deep finder .
   *
   * @param mysterio {@link Class} or an instance of any {@link Object}. maybe null
   * @return all fields declared in class of the object or super class
   */
  static Field[] getFields(final Object mysterio) {
    return familyze(mysterio).map(Class::getDeclaredFields).flatMap(Arrays::stream).toArray(Field[]::new);
  }

  /**
   * test if the value is able to set the field .
   *
   * @param field {@link Field}
   * @param value the value
   * @return true if the value is able to set the field, or returns false if that is not able to
   */
  static boolean isAssignable(final Field field, final Object value) {
    // @formatter:off
    return field == null ? false :
      field.getType().isPrimitive() && value == null ? false :
      isAssignable(field.getType(), getClass(value))
    // @formatter:on
    ;
  }

  /**
   * test if the field is declared in class of the object or super class .
   *
   * @param mysterio {@link Class} or the Instance
   * @param field {@link Field}
   * @return true if the field is declared in class of the object or super class, or returns false if that is not
   */
  static boolean isAssignable(final Object mysterio, final Field field) {
    final Class<?> theClass = getClass(mysterio);
    // @formatter:off
    return field != null && theClass != null && field.getDeclaringClass().isAssignableFrom(theClass);
    // @formatter:on
  }
}