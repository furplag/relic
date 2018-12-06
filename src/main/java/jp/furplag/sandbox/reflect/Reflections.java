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

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import jp.furplag.function.ThrowableBiPredicate;
import jp.furplag.function.ThrowableFunction;
import jp.furplag.function.ThrowablePredicate;
import jp.furplag.sandbox.stream.Streamr;

/**
 * code snippet for accessing object .
 *
 * @author furplag
 *
 */
public interface Reflections {

  /**
   * execute {@link AccessibleObject#trySetAccessible()} stealithly .
   *
   * @param <T> any of {@link AccessibleObject}
   * @param accessibleObject {@link AccessibleObject}
   * @return {@code accessibleObject}
   */
  static <T extends AccessibleObject> T conciliation(final T accessibleObject) {
    return Streamr.stream(accessibleObject).peek(AccessibleObject::trySetAccessible).findAny().orElse(accessibleObject);
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
   * Returns annotations that are present on this element .
   * <p>If there are no annotations <em>present</em> on this element, the return value is {@link Collections#emptySet()} .</p>
   *
   * @param <E> {@link AnnotatedElement}
   * @param annotatedElement any of {@link AnnotatedElement}, maybe null
   * @return annotations that are present on this element
   */
  private static <E extends AnnotatedElement> Set<Class<?>> getAnnotations(final E annotatedElement) {
    return ThrowableFunction.orElseGet(annotatedElement, (e) -> Streamr.collect(Streamr.stream(e.getAnnotations()).map(Annotation::annotationType), HashSet::new), Collections::emptySet);
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
    return Streamr.Filter.filtering(getFields(mysterio), (f) -> StringUtils.equals(fieldName, f.getName())).findFirst().orElse(null);
  }

  /**
   * {@link Class#getDeclaredFields()} with deep finder .
   *
   * @param mysterio {@link Class} or an instance of any {@link Object}. maybe null
   * @return all fields declared in class of the object or super class
   */
  static Field[] getFields(final Object mysterio) {
    return familyze(mysterio).map(Class::getDeclaredFields).flatMap(Arrays::stream).map(Reflections::conciliation).toArray(Field[]::new);
  }

  /**
   * test if the element is annotated with any of specified {@link Annotation annotation (s) } .
   *
   * @param <E> {@link AnnotatedElement}
   * @param annotatedElement any of {@link AnnotatedElement}, maybe null
   * @param annotations {@link Annotation}, maybe empty
   * @return true if the element is annotated with any of specified {@link Annotation annotation (s) }
   */
  @SafeVarargs
  static <E extends AnnotatedElement> boolean isAnnotatedWith(final E annotatedElement, final Class<? extends Annotation>... annotations) {
    return ThrowableBiPredicate.orNot(
        getAnnotations(annotatedElement), Streamr.stream(annotations).collect(Collectors.toSet()), (e, f) -> f.isEmpty() || f.stream().anyMatch(e::contains));
  }

  /**
   * test if the element is annotated with all of specified {@link Annotation annotation (s) } .
   *
   * @param <E> {@link AnnotatedElement}
   * @param annotatedElement any of {@link AnnotatedElement}, maybe null
   * @param annotations {@link Annotation}, maybe empty
   * @return true if the element is annotated with all of specified {@link Annotation annotation (s) }
   */
  @SafeVarargs
  static <E extends AnnotatedElement> boolean isAnnotatedWithAllOf(final E annotatedElement, final Class<? extends Annotation>... annotations) {
    return ThrowableBiPredicate.orNot(getAnnotations(annotatedElement), Streamr.stream(annotations).collect(Collectors.toSet()), (e, f) -> e.containsAll(f) && f.containsAll(e));
  }

  /**
   * {@link #isAssignable(Field, Object)} using this method if the value type is standard object .
   *
   * @param typeOfFiled type of the field
   * @param typeOfValue type of the value
   * @return true if the value is able to set the field, or returns false if that is not able to
   */
  static boolean isAssignable(final Class<?> typeOfFiled, final Class<?> typeOfValue) {
    final BiPredicate<Class<?>, Class<?>> isConvertible = (f, v) -> String.class.equals(f) || (ClassUtils.isAssignable(v, f)) || Streamr.stream(ClassUtils.primitivesToWrappers(f, v)).allMatch(Number.class::isAssignableFrom);

    return ThrowableBiPredicate.orNot(typeOfFiled, typeOfValue, ((ThrowableBiPredicate<Class<?>, Class<?>>) Reflections::notAssignable).negate().and(isConvertible));
  }

  /**
   * test if the value is able to set the field .
   *
   * @param field {@link Field}
   * @param value the value
   * @return true if the value is able to set the field, or returns false if that is not able to
   */
  static boolean isAssignable(final Field field, final Object value) {
    return field != null && isAssignable(field.getType(), getClass(value));
  }

  /**
   * test if the field is declared in class of the object or super class .
   *
   * @param mysterio {@link Class} or the Instance
   * @param field {@link Field}
   * @return true if the field is declared in class of the object or super class, or returns false if that is not
   */
  static boolean isAssignable(final Object mysterio, final Field field) {
    return ThrowableBiPredicate.orNot(mysterio, field, (o, f) -> f.getDeclaringClass().isAssignableFrom(getClass(o)));
  }

  /**
   * test if the value is able to set the field of the object .
   *
   * @param mysterio {@link Class} or the Instance
   * @param field {@link Field}
   * @param value the value
   * @return true if the value is able to set the field of the object, or returns false if that is not able to
   */
  static boolean isAssignable(final Object mysterio, final Field field, final Object value) {
    return isAssignable(mysterio, field) && isAssignable(field, value);
  }

  /**
   * shorthand for {@link Modifier#isStatic(int)} .
   *
   * @param field {@link Field}
   * @return the result of {@link Modifier#isStatic(int) Modifier#isStatic}({@link Field#getModifiers() modifier}) .
   */
  static boolean isStatic(final Field field) {
    return ThrowablePredicate.orNot(field, (t) ->Modifier.isStatic(t.getModifiers()));
  }

  /**
   * a part of {@link #isAssignable(Class, Class)} .
   *
   * @param typeOfFiled type of the field
   * @param typeOfValue type of the value
   * @return true if the value is not able to set the field, or returns false if that is able to
   */
  private static boolean notAssignable(final Class<?> typeOfFiled, final Class<?> typeOfValue) {
    // @formatter:off
    return typeOfFiled == null || (typeOfFiled.isPrimitive() && typeOfValue == null) ||
      (Number.class.isAssignableFrom(ClassUtils.primitiveToWrapper(typeOfFiled)) && Character.class.equals(ClassUtils.primitiveToWrapper(typeOfValue)));
    // @formatter:on
  }
}
